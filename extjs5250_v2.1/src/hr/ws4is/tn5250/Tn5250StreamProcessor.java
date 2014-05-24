/*
 * Copyright (C) 2014,  Tomislav Milkovic
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 */
package hr.ws4is.tn5250;

import hr.ws4is.data.tn5250.Tn5250ScreenResponse;
import hr.ws4is.data.tn5250.Tn5250ScreenElement;
import hr.ws4is.data.tn5250.Tn5250ScreenRequest;

import java.util.List;
import java.util.regex.Pattern;

import javax.enterprise.inject.Vetoed;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tn5250j.Session5250;
import org.tn5250j.TN5250jConstants;
import org.tn5250j.framework.tn5250.Screen5250;
import org.tn5250j.framework.tn5250.ScreenField;
import org.tn5250j.framework.tn5250.ScreenFields;
import org.tn5250j.framework.tn5250.ScreenOIA;

/**
 * Main 5250 stream processor which converts 5250 screen data into web data.
 */
@Vetoed
enum Tn5250StreamProcessor {
    ;

    private static final Logger LOGGER = LoggerFactory.getLogger(Tn5250StreamProcessor.class);
    private static final Pattern LTRIM = Pattern.compile("^\\s+");
    private static final Pattern RTRIM = Pattern.compile("\\s+$");

    public static String ltrim(final String s) {
        return LTRIM.matcher(s).replaceAll("");
    }

    public static String rtrim(final String s) {
        return RTRIM.matcher(s).replaceAll("");
    }

    /*
     * Refresh current screen.
     */
    public static void refresh(final Session5250 session, final Tn5250ScreenResponse response) {
        final Screen5250 screen = session.getScreen();
        final ScreenOIA oia = screen.getOIA();
        final List<Tn5250ScreenElement> list = getResponse(session);
        response.setData(list);
        response.setSize(screen.getColumns());
        response.setLocked(oia.isKeyBoardLocked());
        response.setMsgw(oia.isMessageWait());
    }

    /*
     * forward request from web to host and waits for return after response,
     * screen will be reloaded TODO - handle session hang & timeout so not to
     * continue
     */
    public static boolean process(final Session5250 session, final Tn5250ScreenRequest request, final Tn5250ScreenElement[] fields) {

        boolean sendIt = false;

        // Parameter PF set in javascript by key press
        String aidS = request.getKeyRequest();
        if (aidS != null && aidS.length() > 0) {
            sendIt = true;
        }

        if (aidS != null && aidS.length() > 0) {
            aidS = "[" + aidS.toLowerCase() + "]";
        } else {
            aidS = TN5250jConstants.MNEMONIC_ENTER;
        }

        if (aidS.equals(TN5250jConstants.MNEMONIC_SYSREQ)) {
            if (request.getData() == null) {
                return false;
            }
            session.getVT().systemRequest(request.getData());
            return true;
        }

        final Screen5250 screen = session.getScreen();
        // fill screen fields from web data
        if (updateFieldValues(screen, fields)) {
            sendIt = true;
        }

        // get currently cursor positioned field
        processCursor(screen, request.getCursorField());

        sendKeyRequest(screen, sendIt, aidS);

        return true;
    }

    // sends request from web to host
    private static void sendKeyRequest(final Screen5250 screen, final boolean sendIt, final String aidS) {
        final ScreenOIA oia = screen.getOIA();
        if (sendIt || screen.getScreenFields().getFieldCount() == 0) {
            screen.sendKeys(aidS);
            while (oia.getInputInhibited() == ScreenOIA.INPUTINHIBITED_SYSTEM_WAIT && oia.getLevel() != ScreenOIA.OIA_LEVEL_INPUT_ERROR) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    LOGGER.info(ex.getMessage());
                }
            }
        }
    }

    // detect cursor position and update position to 5250 screen
    private static void processCursor(final Screen5250 screen, final int field) {
        int fld = field;
        boolean nofld = fld < 1;

        if (!nofld) {

            if (fld > 1000) {
                fld = (fld - 1000) >> 1;
            }

            final ScreenField sf = screen.getScreenFields().getField(fld);
            if (sf != null) {
                nofld = sf.isBypassField();
            }
        }

        if (nofld) {

        } else if (fld > 0) {

            boolean setc = false;
            if (fld < 1) {
                setc = screen.gotoField(screen.getScreenFields().getSize());
            } else {
                setc = screen.gotoField(fld);
            }
            if (!setc) {
                LOGGER.info("Field not set");
            }
            // screen.gotoField(request.getCursorField());
        }
    }

    // update values from web field to 5250 screen fields
    private static boolean updateFieldValues(final Screen5250 screen, final Tn5250ScreenElement[] webFields) {

        boolean sendIt = false;
        final ScreenFields fields = screen.getScreenFields();

        for (final Tn5250ScreenElement webField : webFields) {
            final String fieldValue = webField.getValue();

            if (fieldValue != null && fieldValue.length() > 0) {
                sendIt = true;
                final ScreenField screenField = fields.getField(webField.getFieldId() - 1);
                screen.setCursor(screenField.startRow(), screenField.startCol());
                screenField.setString(fieldValue);
                // System.out.println("FLD" + (x + 1) + "-> " + field + " -> " +
                // sf.getString());
            } else {
                final ScreenField screenField = fields.getField(webField.getFieldId() - 1);
                screenField.setString("");
            }
        }
        return sendIt;
    }

    // Process response screen and fills up list of screen elements ready to be
    // sent to the browser
    private static List<Tn5250ScreenElement> getResponse(final Session5250 session) {

        final Screen5250 screen = session.getScreen();
        final Tn5250ScreenData screenRect = new Tn5250ScreenData(1, 1, screen);

        // for debug purposes
        // TnScreenData.saveDebug(screenRect);

        int pos = 0;
        while (pos < screenRect.getLenScreen()) {

            screenRect.updateIfColorChanged(pos);
            if (screenRect.isField(pos)) {
                processFields(screenRect, pos);
            } else {
                if (screenRect.isText(pos)) {
                    screenRect.addText(pos);
                } else {
                    screenRect.addSpace();
                }
            }

            screenRect.updateIfAttributeChanged();
            screenRect.updateIfNewLine();

            pos++;
        }
        screenRect.updateScreenElements();
        return screenRect.getScreenElements();
    }

    private static void processFields(final Tn5250ScreenData screenRect, final int pos) {

        final ScreenField screenField = screenRect.findScreenField(pos);
        final boolean process = screenField != null && screenField.startPos() == pos;

        if (process) {
            processField(screenRect, pos, screenField);
        }

    }

    private static void processField(final Tn5250ScreenData screenRect, final int pos, final ScreenField screenField) {
        screenRect.initScreenElement(0);
        screenRect.updateIfHiddenField(pos);

        if (screenField.isBypassField()) {
            screenRect.updateIfHiddenField(pos);
        }

        // if the field will extend past the screen column size
        // we will just truncate it to be the size of the rest
        // of the screen.
        int len = screenField.getLength();
        if (screenRect.getCol() + len > screenRect.getNumCols()) {
            len = screenRect.getNumCols() - screenRect.getCol();
        }

        // get the field contents and only trim the non numeric
        // fields so that the numeric fields show up with
        // the correct alignment within the field.
        final String value = getFieldValue(screenField);
        final int focusfield = getFocusField(screenRect.getScreenFields());
        final int fieldMask = getFieldMask(screenField, focusfield);

        screenRect.getElement().setFieldType(fieldMask);
        screenRect.getElement().setFieldId(screenField.getFieldId());
        screenRect.getElement().setLength(len);
        screenRect.getElement().setMaxLength(len);
        screenRect.getElement().setValue(value);

        if (len < screenField.getLength()) {
            processMultiLineField(screenRect, screenField, len, fieldMask);
        }

    }

    // process 5250 multiline screen fields to web fields
    private static void processMultiLineField(final Tn5250ScreenData screenRect, final ScreenField screenField, final int len, final int fieldMask) {
        int row = screenRect.getRow();
        final int numCols = screenRect.getNumCols();

        final int alen = (screenField.getLength() - len);
        final int al = alen / (screenRect.getNumCols() - 1);
        int ai, astart, astop = 0;

        for (ai = 1; ai <= al; ai++) {
            row++;

            astart = len + (ai - 1) * (numCols - 1);
            astop = astart + (numCols - 1);
            if (astop > alen) {
                astop = alen;
            }
            String aval = screenField.getString().substring(astart, astop);
            if (screenField.isNumeric() || screenField.isSignedNumeric()) {

            } else {
                aval = rtrim(aval);
            }

            int flen = (numCols - 1) * ai;
            if (flen > screenField.getLength()) {
                flen = screenField.getLength() - flen;
            } else {
                flen = numCols - 1;
            }

            screenRect.initScreenElement(0);
            Tn5250ScreenElement element = screenRect.getElement();
            element.setFieldType(fieldMask);
            element.setFieldId(screenField.getFieldId() * 1000 + ai);
            element.setLength(flen);
            element.setMaxLength(flen);
            element.setValue(aval);
            element.setRow(row);
        }
    }

    // get 5250 field value
    private static String getFieldValue(final ScreenField screenField) {
        String value = null;
        if (screenField.isNumeric() || screenField.isSignedNumeric()) {
            value = screenField.getString();
        } else {
            value = rtrim(screenField.getString());
        }
        return value;
    }

    // convert 5250 field statuses to web field mask
    private static int getFieldMask(final ScreenField screenField, final int focusfield) {
        int mask = 0;
        mask = mask | ((focusfield == screenField.getFieldId()) ? 1 : 0);
        mask = mask << 1;
        mask = mask | (screenField.isAutoEnter() ? 1 : 0);
        mask = mask << 1;
        mask = mask | (screenField.isBypassField() ? 1 : 0);
        mask = mask << 1;
        mask = mask | (screenField.isContinued() ? 1 : 0);
        mask = mask << 1;
        mask = mask | (screenField.isContinuedFirst() ? 1 : 0);
        mask = mask << 1;
        mask = mask | (screenField.isContinuedLast() ? 1 : 0);
        mask = mask << 1;
        mask = mask | (screenField.isContinuedMiddle() ? 1 : 0);
        mask = mask << 1;
        mask = mask | (screenField.isDupEnabled() ? 1 : 0);
        mask = mask << 1;
        mask = mask | (screenField.isFER() ? 1 : 0);
        mask = mask << 1;
        mask = mask | (screenField.isHiglightedEntry() ? 1 : 0);
        mask = mask << 1;
        mask = mask | (screenField.isMandatoryEnter() ? 1 : 0);
        mask = mask << 1;
        mask = mask | (screenField.isNumeric() ? 1 : 0);
        mask = mask << 1;
        mask = mask | (screenField.isSignedNumeric() ? 1 : 0);
        mask = mask << 1;
        mask = mask | (screenField.isToUpper() ? 1 : 0);
        return mask;
    }

    // find focused field on 5250 screen
    private static int getFocusField(final ScreenFields screenFields) {

        ScreenField focusField = screenFields.getCurrentField();
        if (focusField == null) {
            focusField = screenFields.getFirstInputField();
        }

        int fieldID = -1;
        if (focusField != null) {
            fieldID = focusField.getFieldId();
        }
        return fieldID;

    }
}
