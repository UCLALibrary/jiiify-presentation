
package info.freelibrary.iiif.presentation;

import java.net.URI;

/**
 * A content resource.
 *
 * @author <a href="mailto:ksclarke@ksclarke.io">Kevin S. Clarke</a>
 */
public class OtherContent extends Content<OtherContent> {

    static final String TYPE = "sc:AnnotationList";

    /**
     * Creates a IIIF presentation content resource.
     *
     * @param aID A other content ID
     * @param aCanvas A canvas for other content
     */
    public OtherContent(final String aID, final Canvas aCanvas) {
        super(TYPE, aID, aCanvas);
    }

    /**
     * Creates a IIIF presentation content resource.
     *
     * @param aID A other content ID
     * @param aCanvas A canvas for other content
     */
    public OtherContent(final URI aID, final Canvas aCanvas) {
        super(TYPE, aID, aCanvas);
    }

}
