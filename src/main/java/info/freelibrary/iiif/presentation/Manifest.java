
package info.freelibrary.iiif.presentation;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;

import info.freelibrary.iiif.presentation.properties.Behavior;
import info.freelibrary.iiif.presentation.properties.Label;
import info.freelibrary.iiif.presentation.properties.Metadata;
import info.freelibrary.iiif.presentation.properties.NavDate;
import info.freelibrary.iiif.presentation.properties.Start;
import info.freelibrary.iiif.presentation.properties.Summary;
import info.freelibrary.iiif.presentation.properties.Thumbnail;
import info.freelibrary.iiif.presentation.properties.ViewingDirection;
import info.freelibrary.iiif.presentation.properties.behaviors.ManifestBehavior;
import info.freelibrary.iiif.presentation.utils.MessageCodes;
import info.freelibrary.util.I18nRuntimeException;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;

/**
 * The overall description of the structure and properties of the digital representation of an object. It carries
 * information needed for the viewer to present the digitized content to the user, such as a title and other
 * descriptive information about the object or the intellectual work that it conveys. Each manifest describes how to
 * present a single object such as a book, a photograph, or a statue.
 */
public class Manifest extends Resource<Manifest> {

    private static final String TYPE = "sc:Manifest";

    private static final int REQ_ARG_COUNT = 3;

    private final List<URI> myContexts = Stream.of(Constants.CONTEXT_URI).collect(Collectors.toList());

    private final List<Sequence> mySequences = new ArrayList<>();

    private NavDate myNavDate;

    private ViewingDirection myViewingDirection;

    private Optional<Start> myStart = Optional.empty();

    /**
     * Creates a IIIF presentation manifest.
     *
     * @param aID A manifest ID in string form
     * @param aLabel A manifest label in string form
     * @throws IllegalArgumentException If the supplied ID is not a valid URI
     */
    public Manifest(final String aID, final String aLabel) {
        super(TYPE, aID, aLabel, REQ_ARG_COUNT);
    }

    /**
     * Creates a IIIF presentation manifest.
     *
     * @param aID A manifest ID
     * @param aLabel A manifest label
     */
    public Manifest(final URI aID, final Label aLabel) {
        super(TYPE, aID, aLabel, REQ_ARG_COUNT);
    }

    /**
     * Creates a IIIF presentation manifest.
     *
     * @param aID A manifest ID in string form
     * @param aLabel A manifest label in string form
     * @param aMetadata A manifest's metadata
     * @param aSummary A manifest summary in string form
     * @param aThumbnail A manifest thumbnail
     * @throws URISyntaxException If the supplied ID is not a valid URI
     */
    public Manifest(final String aID, final String aLabel, final Metadata aMetadata, final String aSummary,
            final Thumbnail aThumbnail) throws URISyntaxException {
        super(TYPE, aID, aLabel, aMetadata, aSummary, aThumbnail, REQ_ARG_COUNT);
    }

    /**
     * Creates a IIIF presentation manifest.
     *
     * @param aID A manifest ID
     * @param aLabel A manifest label
     * @param aMetadata A manifest's metadata
     * @param aSummary A manifest summary
     * @param aThumbnail A manifest thumbnail
     */
    public Manifest(final URI aID, final Label aLabel, final Metadata aMetadata, final Summary aSummary,
            final Thumbnail aThumbnail) {
        super(TYPE, aID, aLabel, aMetadata, aSummary, aThumbnail, REQ_ARG_COUNT);
    }

    /**
     * A private constructor used for serialization purposes.
     */
    private Manifest() {
        super(TYPE);
    }

    @Override
    @JsonSetter(Constants.BEHAVIOR)
    public Manifest setBehaviors(final Behavior... aBehaviorArray) {
        return super.setBehaviors(checkBehaviors(ManifestBehavior.class, aBehaviorArray));
    }

    @Override
    public Manifest addBehaviors(final Behavior... aBehaviorArray) {
        return super.addBehaviors(checkBehaviors(ManifestBehavior.class, aBehaviorArray));
    }

    /**
     * Gets the manifest context.
     *
     * @return The manifest context
     */
    @JsonIgnore
    public List<URI> getContexts() {
        if (myContexts.isEmpty()) {
            return null;
        } else {
            return myContexts;
        }
    }

    /**
     * Gets the first manifest context.
     *
     * @return The manifest context
     */
    @JsonIgnore
    public URI getContext() {
        return Constants.CONTEXT_URI;
    }

    /**
     * Adds an array of new context URIs to the manifest.
     *
     * @param aContextArray Manifest context URIs(s)
     * @return The manifest
     */
    public Manifest addContext(final URI... aContextArray) {
        Collections.addAll(myContexts, aContextArray);
        return this;
    }

    /**
     * Adds an array of new context URIs, in string form, to the manifest.
     *
     * @param aContextArray Manifest context URI(s) in string form
     * @return The manifest
     */
    public Manifest addContext(final String... aContextArray) {
        Objects.requireNonNull(aContextArray, MessageCodes.JPA_007);

        for (final String uri : aContextArray) {
            Objects.requireNonNull(aContextArray, MessageCodes.JPA_007);

            myContexts.add(URI.create(uri));
        }

        return this;
    }

    /**
     * Sets a navigation date.
     *
     * @param aNavDate The navigation date
     * @return The manifest
     */
    @JsonSetter(Constants.NAV_DATE)
    public Manifest setNavDate(final NavDate aNavDate) {
        myNavDate = aNavDate;
        return this;
    }

    /**
     * Gets a navigation date.
     *
     * @return The navigation date
     */
    @JsonGetter(Constants.NAV_DATE)
    public NavDate getNavDate() {
        return myNavDate;
    }

    /**
     * Sets the viewing direction.
     *
     * @param aViewingDirection A viewing direction
     * @return The manifest
     */
    @JsonSetter(Constants.VIEWING_DIRECTION)
    public Manifest setViewingDirection(final ViewingDirection aViewingDirection) {
        myViewingDirection = aViewingDirection;
        return this;
    }

    /**
     * Gets the viewing direction.
     *
     * @return The viewing direction
     */
    @JsonGetter(Constants.VIEWING_DIRECTION)
    public ViewingDirection getViewingDirection() {
        return myViewingDirection;
    }

    /**
     * Adds one or more sequences to the manifest.
     *
     * @param aSequenceArray An array of sequences to add to the manifest
     * @return The manifest
     */
    public Manifest addSequence(final Sequence... aSequenceArray) {
        Objects.requireNonNull(aSequenceArray, MessageCodes.JPA_008);

        for (final Sequence sequence : aSequenceArray) {
            Objects.requireNonNull(sequence, MessageCodes.JPA_008);

            if (!mySequences.add(sequence)) {
                throw new UnsupportedOperationException();
            }
        }

        return this;
    }

    /**
     * Gets the manifest's sequences.
     *
     * @return The manifest's sequences
     */
    @JsonGetter(Constants.SEQUENCES)
    public List<Sequence> getSequences() {
        return mySequences;
    }

    /**
     * Sets the manifest sequences to the supplied one(s).
     *
     * @param aSequenceArray An array of sequences to set
     * @return The manifest
     */
    @JsonGetter(Constants.SEQUENCES)
    public Manifest setSequences(final Sequence... aSequenceArray) {
        mySequences.clear();
        return addSequence(aSequenceArray);
    }

    /**
     * Sets the optional start.
     *
     * @param aStart A start
     * @return The range
     */
    @JsonSetter(Constants.START)
    public Manifest setStart(final Start aStart) {
        myStart = Optional.ofNullable(aStart);
        return this;
    }

    /**
     * Gets the optional start.
     *
     * @return The optional start
     */
    @JsonGetter(Constants.START)
    public Optional<Start> getStart() {
        return myStart;
    }

    /**
     * Returns a JsonObject of the Manifest.
     *
     * @return A JsonObject of the Manifest
     */
    @JsonIgnore
    public JsonObject toJSON() {
        return JsonObject.mapFrom(this);
    }

    @Override
    @JsonIgnore
    public String toString() {
        return toJSON().encodePrettily();
    }

    /**
     * Returns a Manifest from its JSON representation.
     *
     * @param aJsonObject A Manifest in JSON form
     * @return The manifest
     */
    @JsonIgnore
    public static Manifest fromJSON(final JsonObject aJsonObject) {
        return Json.decodeValue(aJsonObject.toString(), Manifest.class);
    }

    /**
     * Returns a Manifest from its JSON representation.
     *
     * @param aJsonString A manifest in string form
     * @return The manifest
     */
    @JsonIgnore
    public static Manifest fromString(final String aJsonString) {
        return fromJSON(new JsonObject(aJsonString));
    }

    /**
     * Method used internally to set context from JSON.
     *
     * @param aContext A manifest context in string form
     */
    @JsonSetter(Constants.CONTEXT)
    private void setContext(final String aContext) {
        if (!Constants.CONTEXT_URI.equals(URI.create(aContext))) {
            throw new I18nRuntimeException();
        }
    }

    /**
     * Gets the manifest context. The manifest can either have a single context or an array of contexts (Cf.
     * https://iiif.io/api/presentation/3.0/#46-linked-data-context-and-extensions)
     *
     * @return The manifest context
     */
    @JsonGetter(Constants.CONTEXT)
    private Object getJsonContext() {
        if (myContexts.size() == 1) {
            return myContexts.get(0);
        } else if (myContexts.size() > 1) {
            return myContexts;
        } else {
            return null;
        }
    }
}
