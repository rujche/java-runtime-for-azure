package com.microsoft.aspire.model.service;

/**
 * Defines address allocation modes for a Service.
 */
public enum AddressAllocationModes {
    // Bind only to 127.0.0.1
    IPV4_ZERO_ONE("IPv4ZeroOne"),
    // Bind to any 127.*.*.* loopback address range
    IPV4_LOOPBACK("IPv4Loopback"),
    // Bind to IPv6 ::1
    IPV6_ZERO_ONE("IPv6ZeroOne"),
    // Bind to "localhost", which is all loopback devices on the machine.
    LOCALHOST("Localhost"),
    // Don't use a proxy, instead bind to the first Endpoint.
    PROXYLESS("Proxyless");

    private final String mode;

    AddressAllocationModes(String mode) {
        this.mode = mode;
    }

    public String getMode() {
        return mode;
    }
}
