package com.microsoft.aspire.dcp.model.container;

public class PortProtocol {
    public static final String TCP = "TCP";
    public static final String UDP = "UDP";

    /**
     * Canonicalizes the protocol string to uppercase and validates it.
     * @param protocol The protocol string to canonicalize.
     * @return The canonicalized protocol if valid.
     * @throws IllegalArgumentException if the protocol is not valid.
     */
    public static String canonicalize(String protocol) {
        String protocolUC = protocol.toUpperCase();
        switch (protocolUC) {
            case TCP:
            case UDP:
                return protocolUC;
            default:
                throw new IllegalArgumentException("Port protocol value must be 'TCP' or 'UDP'");
        }
    }

//    /**
//     * Converts a string protocol to its corresponding ProtocolType enum.
//     * @param protocol The protocol string.
//     * @return The corresponding ProtocolType.
//     * @throws IllegalArgumentException if the protocol is unsupported.
//     */
//    public static ProtocolType toProtocolType(String protocol) {
//        String canonical = canonicalize(protocol);
//        switch (canonical) {
//            case TCP:
//                return ProtocolType.TCP;
//            case UDP:
//                return ProtocolType.UDP;
//            default:
//                throw new IllegalArgumentException("Supported protocols are TCP and UDP");
//        }
//    }
//
//    /**
//     * Converts a ProtocolType to its string representation.
//     * @param protocolType The ProtocolType.
//     * @return The string representation of the protocol.
//     * @throws IllegalArgumentException if the protocol type is unsupported.
//     */
//    public static String fromProtocolType(ProtocolType protocolType) {
//        switch (protocolType) {
//            case TCP:
//                return TCP;
//            case UDP:
//                return UDP;
//            default:
//                throw new IllegalArgumentException("Supported protocols are TCP and UDP");
//        }
//    }

//    // Enum to represent protocol types
//    public enum ProtocolType {
//        TCP, UDP
//    }
}
