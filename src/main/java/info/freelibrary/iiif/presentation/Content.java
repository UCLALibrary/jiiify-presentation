
package info.freelibrary.iiif.presentation;

import java.net.URI;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;

/**
 * A package-level content class that extends Resource and is extended by other public classes.
 *
 * @param <T> The class that's extending this content
 */
class Content<T extends Content<T>> extends Resource<T> {

    private static final int REQ_ARG_COUNT = 2;

    private URI myOn;

    /**
     * Creates a IIIF presentation content resource.
     *
     * @param aType The type in string form
     * @param aID The ID in string form
     * @param aCanvas A canvas
     */
    protected Content(final String aType, final String aID, final Canvas aCanvas) {
        super(aType, aID, REQ_ARG_COUNT);
        myOn = aCanvas.getID();
    }

    /**
     * Creates a IIIF presentation content resource.
     *
     * @param aType The type in string form
     * @param aID A URI ID
     * @param aCanvas A canvas
     */
    protected Content(final String aType, final URI aID, final Canvas aCanvas) {
        super(aType, aID, REQ_ARG_COUNT);
        myOn = aCanvas.getID();
    }

    /**
     * Creates a IIIF presentation content resource.
     *
     * @param aType A type of Content
     */
    protected Content(final String aType) {
        super(aType);
    }

    @JsonGetter(Constants.ON)
    public URI getOn() {
        return myOn;
    }

    @JsonIgnore
    public T setOn(final URI aURI) {
        myOn = aURI;
        return (T) this;
    }

    @JsonSetter(Constants.ON)
    public T setOn(final String aURI) {
        myOn = URI.create(aURI);
        return (T) this;
    }

}
