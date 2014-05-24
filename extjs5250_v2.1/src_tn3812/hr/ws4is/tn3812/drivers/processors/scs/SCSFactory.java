package hr.ws4is.tn3812.drivers.processors.scs;

import hr.ws4is.tn3812.drivers.processors.IProcessor;
import hr.ws4is.tn3812.drivers.processors.ProcessorType;

/**
 * Factory for SCS data stream.
 * THere might be different processors / data parsers.
 * On AS/400 exists multiple versions of SCS so we can create
 * specific parsers based on dta version.
 */
public enum SCSFactory {
    ;

    public static IProcessor get(final ProcessorType type) {

        IProcessor processor = null;
        switch (type) {
        case SCS:
            processor = new SCSProcessor();
            break;
        case SCS_LOGGER:
            processor = new SCSLogger();
            break;
        default:
            break;
        }
        return processor;
    }
}
