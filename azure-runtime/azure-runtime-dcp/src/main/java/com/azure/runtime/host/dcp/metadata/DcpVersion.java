package com.azure.runtime.host.dcp.metadata;

import com.azure.runtime.host.dcp.utils.Version;

public class DcpVersion
{
    public static Version MinimumVersionInclusive = new Version(0, 2, 3); // Aspire GA (8.0) release
    public static Version MinimumVersionAspire_8_1 = new Version(0, 5, 6);

    /// <summary>
    /// Development build version proxy, considered always "current" and supporting latest features. 
    /// </summary>
    public static Version Dev = new Version(1000, 0, 0);
}
