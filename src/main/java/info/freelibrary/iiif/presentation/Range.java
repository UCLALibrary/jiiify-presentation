
package info.freelibrary.iiif.presentation;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import info.freelibrary.iiif.presentation.properties.Behavior;
import info.freelibrary.iiif.presentation.properties.Homepage;
import info.freelibrary.iiif.presentation.properties.Label;
import info.freelibrary.iiif.presentation.properties.Logo;
import info.freelibrary.iiif.presentation.properties.Metadata;
import info.freelibrary.iiif.presentation.properties.PartOf;
import info.freelibrary.iiif.presentation.properties.Rendering;
import info.freelibrary.iiif.presentation.properties.RequiredStatement;
import info.freelibrary.iiif.presentation.properties.SeeAlso;
import info.freelibrary.iiif.presentation.properties.StartSelector;
import info.freelibrary.iiif.presentation.properties.Summary;
import info.freelibrary.iiif.presentation.properties.ViewingDirection;
import info.freelibrary.iiif.presentation.properties.behaviors.RangeBehavior;
import info.freelibrary.iiif.presentation.utils.MessageCodes;
import info.freelibrary.util.I18nRuntimeException;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;

/**
 * An ordered list of canvases, and/or further ranges. Ranges allow canvases, or parts thereof, to be grouped together
 * in some way. This could be for textual reasons, such as to distinguish books, chapters, verses, sections,
 * non-content-bearing pages, the table of contents or similar. Equally, physical features might be important such as
 * quires or gatherings, sections that have been added later and so forth.
 */
public class Range extends NavigableResource<Range> implements Resource<Range> {

    private final List<Item> myItems = new ArrayList<>();

    private Optional<AccompanyingCanvas> myAccompanyingCanvas;

    private Optional<PlaceholderCanvas> myPlaceholderCanvas;

    private Optional<StartSelector> myStartSelector = Optional.empty();

    private ViewingDirection myViewingDirection;

    /**
     * Creates a new range.
     *
     * @param aID A range ID in string form
     */
    public Range(final String aID) {
        super(ResourceTypes.RANGE, URI.create(aID));
    }

    /**
     * Creates a new range.
     *
     * @param aID A range ID
     */
    public Range(final URI aID) {
        super(ResourceTypes.RANGE, aID);
    }

    /**
     * Creates a new range.
     *
     * @param aID A range ID in string form
     * @param aLabel A descriptive label, in string form, for the range
     */
    public Range(final String aID, final String aLabel) {
        super(ResourceTypes.RANGE, aID, aLabel);
    }

    /**
     * Creates a new range.
     *
     * @param aID A range ID
     * @param aLabel A descriptive label for the range
     */
    public Range(final URI aID, final Label aLabel) {
        super(ResourceTypes.RANGE, aID, aLabel);
    }

    /**
     * Allows Jackson to create a new range during deserialization.
     */
    private Range() {
        super(ResourceTypes.RANGE);
    }

    /**
     * Gets the placeholder canvas.
     *
     * @return A placeholder canvas
     */
    @JsonGetter(Constants.PLACEHOLDER_CANVAS)
    public Optional<PlaceholderCanvas> getPlaceholderCanvas() {
        return myPlaceholderCanvas;
    }

    /**
     * Sets the placeholder canvas
     *
     * @param aCanvas A placeholder canvas
     * @return This range
     */
    @JsonSetter(Constants.PLACEHOLDER_CANVAS)
    public Range setPlaceholderCanvas(final PlaceholderCanvas aCanvas) {
        myPlaceholderCanvas = Optional.ofNullable(aCanvas);
        return this;
    }

    /**
     * Gets the accompanying canvas.
     *
     * @return The accompanying canvas
     */
    @JsonGetter(Constants.ACCOMPANYING_CANVAS)
    public Optional<AccompanyingCanvas> getAccompanyingCanvas() {
        return myAccompanyingCanvas;
    }

    /**
     * Sets the accompanying canvas.
     *
     * @param aCanvas An accompanying canvas
     * @return This range
     */
    @JsonSetter(Constants.ACCOMPANYING_CANVAS)
    public Range setAccompanyingCanvas(final AccompanyingCanvas aCanvas) {
        myAccompanyingCanvas = Optional.ofNullable(aCanvas);
        return this;
    }

    @Override
    public Range clearBehaviors() {
        return (Range) super.clearBehaviors();
    }

    @Override
    @JsonSetter(Constants.BEHAVIOR)
    public Range setBehaviors(final Behavior... aBehaviorArray) {
        return (Range) super.setBehaviors(checkBehaviors(RangeBehavior.class, true, aBehaviorArray));
    }

    @Override
    @JsonSetter(Constants.BEHAVIOR)
    public Range setBehaviors(final List<Behavior> aBehaviorList) {
        return (Range) super.setBehaviors(checkBehaviors(RangeBehavior.class, true, aBehaviorList));
    }

    @Override
    public Range addBehaviors(final Behavior... aBehaviorArray) {
        return (Range) super.addBehaviors(checkBehaviors(RangeBehavior.class, false, aBehaviorArray));
    }

    @Override
    public Range addBehaviors(final List<Behavior> aBehaviorList) {
        return (Range) super.addBehaviors(checkBehaviors(RangeBehavior.class, false, aBehaviorList));
    }

    /**
     * Sets the optional start selector.
     *
     * @param aStartSelector A start selector
     * @return The range
     */
    @JsonSetter(Constants.START)
    public Range setStartSelector(final StartSelector aStartSelector) {
        myStartSelector = Optional.ofNullable(aStartSelector);
        return this;
    }

    /**
     * Gets the optional start selector.
     *
     * @return The optional start selector
     */
    @JsonGetter(Constants.START)
    public Optional<StartSelector> getStartSelector() {
        return myStartSelector;
    }

    /**
     * Sets the viewing direction.
     *
     * @param aViewingDirection A viewing direction
     * @return The range
     */
    @JsonSetter(Constants.VIEWING_DIRECTION)
    public Range setViewingDirection(final ViewingDirection aViewingDirection) {
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
     * Gets a list of the range's items.
     *
     * @return A list of range items
     */
    @JsonGetter(Constants.ITEMS)
    public List<Item> getItems() {
        return myItems;
    }

    /**
     * Sets the range's items.
     *
     * @param aItemList A list of items
     * @return This range
     */
    @JsonSetter(Constants.ITEMS)
    public Range setItems(final List<Item> aItemList) {
        myItems.clear();
        myItems.addAll(aItemList);
        return this;
    }

    /**
     * Clears the currently set items from this range.
     */
    public Range clearItems() {
        myItems.clear();
        return this;
    }

    @Override
    public Range setSeeAlsoRefs(final SeeAlso... aSeeAlsoArray) {
        return (Range) super.setSeeAlsoRefs(aSeeAlsoArray);
    }

    @Override
    public Range setSeeAlsoRefs(final List<SeeAlso> aSeeAlsoList) {
        return (Range) super.setSeeAlsoRefs(aSeeAlsoList);
    }

    @Override
    public Range setPartOfs(final PartOf... aPartOfArray) {
        return (Range) super.setPartOfs(aPartOfArray);
    }

    @Override
    public Range setPartOfs(final List<PartOf> aPartOfList) {
        return (Range) super.setPartOfs(aPartOfList);
    }

    @Override
    public Range setRenderings(final Rendering... aRenderingArray) {
        return (Range) super.setRenderings(aRenderingArray);
    }

    @Override
    public Range setRenderings(final List<Rendering> aRenderingList) {
        return (Range) super.setRenderings(aRenderingList);
    }

    @Override
    public Range setHomepages(final Homepage... aHomepageArray) {
        return (Range) super.setHomepages(aHomepageArray);
    }

    @Override
    public Range setHomepages(final List<Homepage> aHomepageList) {
        return (Range) super.setHomepages(aHomepageList);
    }

    @Override
    public Range setThumbnails(final Thumbnail... aThumbnailArray) {
        return (Range) super.setThumbnails(aThumbnailArray);
    }

    @Override
    public Range setThumbnails(final List<Thumbnail> aThumbnailList) {
        return (Range) super.setThumbnails(aThumbnailList);
    }

    @Override
    public Range setID(final String aID) {
        return (Range) super.setID(aID);
    }

    @Override
    public Range setID(final URI aID) {
        return (Range) super.setID(aID);
    }

    @Override
    public Range setLogo(final String aLogo) {
        return (Range) super.setLogo(aLogo);
    }

    @Override
    public Range setLogo(final Logo aLogo) {
        return (Range) super.setLogo(aLogo);
    }

    @Override
    public Range setRights(final String... aRightsArray) {
        return (Range) super.setRights(aRightsArray);
    }

    @Override
    public Range setRights(final URI... aRightsArray) {
        return (Range) super.setRights(aRightsArray);
    }

    @Override
    public Range setRights(final List<URI> aRightsList) {
        return (Range) super.setRights(aRightsList);
    }

    @Override
    public Range setRequiredStatement(final RequiredStatement aStatement) {
        return (Range) super.setRequiredStatement(aStatement);
    }

    @Override
    public Range setSummary(final String aSummary) {
        return (Range) super.setSummary(aSummary);
    }

    @Override
    public Range setSummary(final Summary aSummary) {
        return (Range) super.setSummary(aSummary);
    }

    @Override
    public Range setMetadata(final Metadata aMetadata) {
        return (Range) super.setMetadata(aMetadata);
    }

    @Override
    public Range setLabel(final String aLabel) {
        return (Range) super.setLabel(aLabel);
    }

    @Override
    public Range setLabel(final Label aLabel) {
        return (Range) super.setLabel(aLabel);
    }

    /**
     * Returns a JsonObject of the Range.
     *
     * @return A JSON representation of the range
     */
    public JsonObject toJSON() {
        return JsonObject.mapFrom(this);
    }

    @Override
    public String toString() {
        return toJSON().encode();
    }

    /**
     * Returns a Range from its JSON representation.
     *
     * @param aJsonObject A range in JSON form
     * @return The range
     */
    @JsonIgnore
    public static Range fromJSON(final JsonObject aJsonObject) {
        return Json.decodeValue(aJsonObject.toString(), Range.class);
    }

    /**
     * Returns a Range from its JSON representation.
     *
     * @param aJsonString A range in string form
     * @return The range
     */
    @JsonIgnore
    public static Range fromString(final String aJsonString) {
        return fromJSON(new JsonObject(aJsonString));
    }

    /**
     * A wrapper for the types of resources that can be put into a range's items: Canvas, Range, and SpecificResource.
     */
    @JsonDeserialize(using = RangeItemDeserializer.class)
    public static class Item {

        private Canvas myCanvas;

        private Range myRange;

        private SpecificResource mySpecificResource;

        /**
         * Creates a new range item from a canvas. The range item represents a reference to the canvas rather than an
         * embedded canvas.
         *
         * @param aCanvas A canvas to use as a range item
         */
        public Item(final Canvas aCanvas) {
            this(aCanvas, false);
        }

        /**
         * Creates a new range item from a canvas; the canvas is either embedded or referenced depending on the
         * boolean flag passed to the constructor.
         *
         * @param aCanvas A canvas to use as a range item
         * @param aEmbeddedCanvas Whether a canvas should be embedded or a reference to it created
         */
        public Item(final Canvas aCanvas, final boolean aEmbeddedCanvas) {
            if (aEmbeddedCanvas) {
                myCanvas = aCanvas;
            } else {
                myCanvas = new Canvas(aCanvas.getID());
            }
        }

        /**
         * Creates a new range item from another range.
         *
         * @param aRange
         */
        public Item(final Range aRange) {
            myRange = aRange;
        }

        /**
         * Creates a new range item from a specific resource
         *
         * @param aSpecificResource
         */
        public Item(final SpecificResource aSpecificResource) {
            mySpecificResource = aSpecificResource;
        }

        /**
         * Gets the ID from the resource wrapped by this item.
         *
         * @return A resource ID
         */
        @JsonIgnore
        public URI getID() {
            if (mySpecificResource != null) {
                return mySpecificResource.getID();
            } else if (myCanvas != null) {
                return myCanvas.getID();
            } else if (myRange != null) {
                return myRange.getID();
            } else {
                throw new I18nRuntimeException(Constants.BUNDLE_NAME, MessageCodes.JPA_040);
            }
        }

        /**
         * Gets the type of the resource wrapped by this item.
         *
         * @return The resource type
         */
        @JsonIgnore
        public String getType() {
            if (mySpecificResource != null) {
                return mySpecificResource.getType();
            } else if (myCanvas != null) {
                return myCanvas.getType();
            } else if (myRange != null) {
                return myRange.getType();
            } else {
                throw new I18nRuntimeException(Constants.BUNDLE_NAME, MessageCodes.JPA_040);
            }
        }

        /**
         * Gets the resource wrapped by this item.
         *
         * @return The item's resource
         */
        @JsonValue
        public Object getResource() {
            if (mySpecificResource != null) {
                return mySpecificResource;
            } else if (myCanvas != null) {
                return myCanvas;
            } else if (myRange != null) {
                return myRange;
            } else {
                throw new I18nRuntimeException(Constants.BUNDLE_NAME, MessageCodes.JPA_040);
            }
        }
    }
}
