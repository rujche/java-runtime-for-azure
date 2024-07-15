
/**
 * An Aspire extension providing support for <a href="https://spring.io">Spring</a> projects.
 */
module com.microsoft.aspire.extensions.manifest.spring {
    requires transitive com.microsoft.aspire.extensions.spring;
    requires manifold.rt;
    requires manifold.ext.rt;

    exports spring.manifest.extensions.com.microsoft.aspire.DistributedApplication;
}