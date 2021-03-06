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
package hr.ws4is.tn3812;

import hr.ws4is.tn3812.interfaces.ITn3812Config;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main incoming data handler, used in process of protocol negotiation. Process
 * printer setup informations, after success, thread is switched to
 * IncomingDataProcessing callback handler.
 */
class IncomingDataNegotiation extends BasicCompletionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(IncomingDataNegotiation.class);

    // private static Logger logger =
    // LoggerFactory.getLogger(IncomingData.class);

    private boolean negotiate = true;

    public IncomingDataNegotiation(final Tn3812Context ctx) {
        super(ctx);
    }

    public void reset() {
        negotiate = true;
    }

    public void failed(final Throwable exc, final Void attachment) {
        exc.printStackTrace();
    }

    public void completed(final Integer result, final Void attachment) {

        try {
            if (handleClosed(result, attachment)) {
                processCompleted(result, attachment);
            }
        } catch (IOException ioException) {
            LOGGER.error(ioException.getMessage(), ioException);
        } catch (InterruptedException interruptedException) {
            LOGGER.error(interruptedException.getMessage(), interruptedException);
        } catch (ExecutionException executionException) {
            LOGGER.error(executionException.getMessage(), executionException);
        }

    }

    // --------------- PRIVATE SECTION ---------------

    private void processCompleted(final Integer result, final Void attachment) throws IOException, InterruptedException, ExecutionException {

        final ByteBuffer buffer = ByteBuffer.allocate(8192);
        final byte[] data = BasicCompletionHandler.readFromBuffer(ctx, result);

        if (negotiate) {
            negotiate = negotiate(buffer, data);
        }

        if (negotiate) {
            BasicCompletionHandler.writeToBuffer(ctx, buffer);
            BasicCompletionHandler.startWriting(ctx);
            BasicCompletionHandler.startReading(ctx, this);
        } else {
            final IncomingDataProcessing handler = ctx.getIncomingProcessingDataHandler();
            handler.completed(result, attachment);
        }
    }

    // --------------- NEGOTIATION SECTION ---------------

    private boolean negotiate(final ByteBuffer buffer, final byte[] abyte0) throws IOException {
        int i = 0;

        // from server negotiations
        if (abyte0[i] == Tn3812Constants.IAC) { // -1

            while (i < abyte0.length && abyte0[i++] == -1) {
                switch (abyte0[i++]) {
                // we will not worry about what it WONT do
                case Tn3812Constants.WONT: // -4
                default:
                    break;

                case Tn3812Constants.DO: // -3

                    if (i < abyte0.length) {
                        switch (abyte0[i]) {
                        case Tn3812Constants.TERMINAL_TYPE: // 24
                            writeIAC(buffer, Tn3812Constants.WILL, Tn3812Constants.TERMINAL_TYPE);
                            break;

                        case Tn3812Constants.OPT_END_OF_RECORD: // 25
                            writeIAC(buffer, Tn3812Constants.WILL, Tn3812Constants.OPT_END_OF_RECORD);
                            break;

                        case Tn3812Constants.TRANSMIT_BINARY: // 0
                            writeIAC(buffer, Tn3812Constants.WILL, Tn3812Constants.TRANSMIT_BINARY);
                            break;

                        case Tn3812Constants.TIMING_MARK: // 6 rfc860
                            writeIAC(buffer, Tn3812Constants.WONT, Tn3812Constants.TIMING_MARK);
                            break;

                        case Tn3812Constants.NEW_ENVIRONMENT: // 39 rfc1572
                            if (ctx.getConfig().getDevName() == null) {
                                writeIAC(buffer, Tn3812Constants.WONT, Tn3812Constants.NEW_ENVIRONMENT);
                            } else {
                                writeIAC(buffer, Tn3812Constants.WILL, Tn3812Constants.NEW_ENVIRONMENT);
                            }
                            break;

                        default: // every thing else we will not do at this time
                            writeIAC(buffer, Tn3812Constants.WONT, abyte0[i]);
                            break;
                        }
                    }

                    i++;
                    break;

                case Tn3812Constants.WILL:

                    switch (abyte0[i]) {
                    case Tn3812Constants.OPT_END_OF_RECORD: // 25
                        writeIAC(buffer, Tn3812Constants.DO, Tn3812Constants.OPT_END_OF_RECORD);
                        break;

                    case Tn3812Constants.TRANSMIT_BINARY: // '\0'
                        writeIAC(buffer, Tn3812Constants.DO, Tn3812Constants.TRANSMIT_BINARY);
                        break;
                    default:
                        break;
                    }
                    i++;
                    break;

                case Tn3812Constants.SB: // -6

                    if (abyte0[i] == Tn3812Constants.NEW_ENVIRONMENT && abyte0[i + 1] == 1) {
                        negNewEnvironment(buffer);

                        while (++i < abyte0.length && abyte0[i + 1] != Tn3812Constants.IAC) {

                        }
                    }

                    if (abyte0[i] == Tn3812Constants.TERMINAL_TYPE && abyte0[i + 1] == 1) {
                        writeTerminalType(buffer);
                        i++;
                    }
                    i++;
                    break;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    private void writeIAC(final ByteBuffer buffer, final byte parm1, final byte parm2) throws IOException {
        buffer.put(Tn3812Constants.IAC);
        buffer.put(parm1);
        buffer.put(parm2);
    }

    private void writeTerminalType(final ByteBuffer buffer) throws IOException {
        buffer.put(Tn3812Constants.IAC);
        buffer.put(Tn3812Constants.SB);
        buffer.put(Tn3812Constants.TERMINAL_TYPE);
        buffer.put(Tn3812Constants.QUAL_IS);
        buffer.put(Tn3812Constants.IBM_3812_1.getBytes());
        buffer.put(Tn3812Constants.IAC);
        buffer.put(Tn3812Constants.SE);
    }

    private byte[] boolToCharByte(final boolean value) {
        return (value ? "1" : "0").getBytes();
    }

    // Negotiate parameters
    /*
     * VAR 0 VALUE 1 NEGOTIATE_ESC 2 USERVAR 3
     * 
     * If a variable or a value contains a VAR, it must be sent as ESC VAR. If a
     * variable or a value contains a USERVAR, it must be sent as ESC USERVAR.
     * If a variable or a value contains a VALUE, it must be sent as ESC VALUE.
     * If a variable or a value contains an ESC, it must be sent as ESC ESC.
     */
    private void negNewEnvironment(final ByteBuffer buffer) throws IOException {

        buffer.put(Tn3812Constants.IAC);
        buffer.put(Tn3812Constants.SB);
        buffer.put(Tn3812Constants.NEW_ENVIRONMENT);
        buffer.put(Tn3812Constants.IS);

        final ITn3812Config config = ctx.getConfig();

        final String devName = config.getDevName();
        if (devName != null) {
            writeUserVar(buffer, "DEVNAME", devName);
        }

        writeUserVar(buffer, "IBMMSGQNAME", config.getIBMMSGQNAME());
        writeUserVar(buffer, "IBMMSGQLIB", config.getIBMMSGQLIB());

        byte[] c = boolToCharByte(config.isIBMTRANSFORM());
        writeUserVar(buffer, "IBMTRANSFORM", c);

        if (config.isIBMTRANSFORM()) {

            writeUserVar(buffer, "IBMMFRTYPMDL", config.getIBMMFRTYPMDL());
            writeUserVar(buffer, "IBMFONT", config.getIBMFONT());
            writeUserVar(buffer, "IBMFORMFEED", config.getIBMFORMFEED());
            writeUserVar(buffer, "IBMPPRSRC1", config.getIBMPPRSRC1().getValue());
            writeUserVar(buffer, "IBMPPRSRC2", config.getIBMPPRSRC2().getValue());
            writeUserVar(buffer, "IBMENVELOPE", config.getIBMENVELOPE().getValue());

            c = boolToCharByte(config.isIBMASCII899());
            writeUserVar(buffer, "IBMASCII899", c);
            writeUserVar(buffer, "IBMWSCSTNAME", config.getIBMWSCSTNAME());
            writeUserVar(buffer, "IBMWSCSTLIB", config.getIBMWSCSTLIB());
        }

        // writeUserVar(buffer, "IBMCODEPAGE", "437");
        // writeUserVar(buffer, "IBMCHARSET", "037");

        writeUserVar(buffer, "IBMBUFFERSIZE", "768");

        buffer.put(Tn3812Constants.IAC);
        buffer.put(Tn3812Constants.SE);

    }

    private void writeUserVar(final ByteBuffer buffer, final String param, final String value) throws IOException {
        if (value != null) {
            writeUserVar(buffer, param, value.getBytes());
        }
    }

    private void writeUserVar(final ByteBuffer buffer, final String param, final byte[] value) throws IOException {

        if (value != null) {
            writeUserVarHead(buffer, param);

            if (value.length > 0) {
                if (value[0] < Tn3812Constants.ESC) {
                    buffer.put(Tn3812Constants.NEGOTIATE_ESC);
                }
            }
            buffer.put(value);
        }
    }

    private void writeUserVar(final ByteBuffer buffer, final String param, final byte value) throws IOException {
        writeUserVarHead(buffer, param);
        if (value < Tn3812Constants.NEGOTIATE_ESC) {
            buffer.put(Tn3812Constants.NEGOTIATE_ESC);
        }
        buffer.put(value);
        if (value == -1) {
            buffer.put(value);
        }
    }

    private void writeUserVarHead(final ByteBuffer buffer, final String param) throws IOException {
        buffer.put(Tn3812Constants.USERVAR);
        buffer.put(param.getBytes());
        buffer.put(Tn3812Constants.VALUE);
    }

}
