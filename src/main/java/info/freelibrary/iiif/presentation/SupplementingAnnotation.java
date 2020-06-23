
package info.freelibrary.iiif.presentation;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;

import info.freelibrary.iiif.presentation.properties.Behavior;
import info.freelibrary.iiif.presentation.properties.Homepage;
import info.freelibrary.iiif.presentation.properties.Label;
import info.freelibrary.iiif.presentation.properties.Metadata;
import info.freelibrary.iiif.presentation.properties.PartOf;
import info.freelibrary.iiif.presentation.properties.Provider;
import info.freelibrary.iiif.presentation.properties.Rendering;
import info.freelibrary.iiif.presentation.properties.RequiredStatement;
import info.freelibrary.iiif.presentation.properties.SeeAlso;
import info.freelibrary.iiif.presentation.properties.Summary;
import info.freelibrary.iiif.presentation.properties.TimeMode;
import info.freelibrary.iiif.presentation.utils.MessageCodes;
import info.freelibrary.util.Logger;
import info.freelibrary.util.LoggerFactory;

/**
 * An annotation used for associating supplementary content resources with a {@link Canvas}.
 */
public class SupplementingAnnotation extends Annotation<SupplementingAnnotation> implements
        Resource<SupplementingAnnotation>, ContentAnnotation<SupplementingAnnotation> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SupplementingAnnotation.class,
            Constants.BUNDLE_NAME);

    private static final String MOTIVATION = "supplementing";

    /**
     * Creates a supplementing annotation.
     *
     * @param aID An ID
     * @param aCanvas A canvas
     */
    protected SupplementingAnnotation(final URI aID, final Canvas aCanvas) {
        super(aID, aCanvas);
        myMotivation = MOTIVATION;
    }

    /**
     * Creates a supplementing annotation.
     *
     * @param aID An ID in string form
     * @param aCanvas A canvas
     */
    protected SupplementingAnnotation(final String aID, final Canvas aCanvas) {
        super(aID, aCanvas);
        myMotivation = MOTIVATION;
    }

    /**
     * Creates a supplementing annotation.
     */
    @SuppressWarnings("unused")
    private SupplementingAnnotation() {
        super();
    }

    @Override
    @JsonSetter(Constants.PROVIDER)
    public SupplementingAnnotation setProviders(final Provider... aProviderArray) {
        return setProviders(Arrays.asList(aProviderArray));
    }

    @Override
    @JsonIgnore
    public SupplementingAnnotation setProviders(final List<Provider> aProviderList) {
        return (SupplementingAnnotation) super.setProviders(aProviderList);
    }

    @Override
    protected void setMotivation(final String aMotivation) {
        if (!MOTIVATION.equals(aMotivation)) {
            throw new IllegalArgumentException(LOGGER.getMessage(MessageCodes.JPA_038, MOTIVATION));
        }

        myMotivation = MOTIVATION;
    }

    @Override
    public SupplementingAnnotation addBody(final ContentResource... aBody) {
        return (SupplementingAnnotation) super.addBody(aBody);
    }

    @Override
    public SupplementingAnnotation addBody(final List<ContentResource> aBody) {
        return addBody(aBody.toArray(new ContentResource[] {}));
    }

    @Override
    public SupplementingAnnotation clearBody() {
        return (SupplementingAnnotation) super.clearBody();
    }

    @Override
    public SupplementingAnnotation setBody(final ContentResource... aBody) {
        return (SupplementingAnnotation) super.setBody(aBody);
    }

    @Override
    public SupplementingAnnotation setBody(final List<ContentResource> aBody) {
        return setBody(aBody.toArray(new ContentResource[] {}));
    }

    @Override
    public SupplementingAnnotation setTarget(final URI aURI) {
        return (SupplementingAnnotation) super.setTarget(aURI);
    }

    @Override
    public SupplementingAnnotation setTarget(final String aURI) {
        return (SupplementingAnnotation) super.setTarget(aURI);
    }

    @Override
    public SupplementingAnnotation clearBehaviors() {
        return (SupplementingAnnotation) super.clearBehaviors();
    }

    @Override
    public SupplementingAnnotation setBehaviors(final Behavior... aBehaviorArray) {
        return (SupplementingAnnotation) super.setBehaviors(aBehaviorArray);
    }

    @Override
    public SupplementingAnnotation setBehaviors(final List<Behavior> aBehaviorList) {
        return (SupplementingAnnotation) super.setBehaviors(aBehaviorList);
    }

    @Override
    public SupplementingAnnotation addBehaviors(final Behavior... aBehaviorArray) {
        return (SupplementingAnnotation) super.addBehaviors(aBehaviorArray);
    }

    @Override
    public SupplementingAnnotation addBehaviors(final List<Behavior> aBehaviorList) {
        return (SupplementingAnnotation) super.addBehaviors(aBehaviorList);
    }

    @Override
    public SupplementingAnnotation setTimeMode(final TimeMode aTimeMode) {
        return (SupplementingAnnotation) super.setTimeMode(aTimeMode);
    }

    @Override
    public SupplementingAnnotation setSeeAlsoRefs(final SeeAlso... aSeeAlsoArray) {
        return (SupplementingAnnotation) super.setSeeAlsoRefs(aSeeAlsoArray);
    }

    @Override
    public SupplementingAnnotation setSeeAlsoRefs(final List<SeeAlso> aSeeAlsoList) {
        return (SupplementingAnnotation) super.setSeeAlsoRefs(aSeeAlsoList);
    }

    @Override
    public SupplementingAnnotation setPartOfs(final PartOf... aPartOfArray) {
        return (SupplementingAnnotation) super.setPartOfs(aPartOfArray);
    }

    @Override
    public SupplementingAnnotation setPartOfs(final List<PartOf> aPartOfList) {
        return (SupplementingAnnotation) super.setPartOfs(aPartOfList);
    }

    @Override
    public SupplementingAnnotation setRenderings(final Rendering... aRenderingArray) {
        return (SupplementingAnnotation) super.setRenderings(aRenderingArray);
    }

    @Override
    public SupplementingAnnotation setRenderings(final List<Rendering> aRenderingList) {
        return (SupplementingAnnotation) super.setRenderings(aRenderingList);
    }

    @Override
    public SupplementingAnnotation setHomepages(final Homepage... aHomepageArray) {
        return (SupplementingAnnotation) super.setHomepages(aHomepageArray);
    }

    @Override
    public SupplementingAnnotation setHomepages(final List<Homepage> aHomepageList) {
        return (SupplementingAnnotation) super.setHomepages(aHomepageList);
    }

    @Override
    public SupplementingAnnotation setThumbnails(final Thumbnail... aThumbnailArray) {
        return (SupplementingAnnotation) super.setThumbnails(aThumbnailArray);
    }

    @Override
    public SupplementingAnnotation setThumbnails(final List<Thumbnail> aThumbnailList) {
        return (SupplementingAnnotation) super.setThumbnails(aThumbnailList);
    }

    @Override
    public SupplementingAnnotation setID(final String aID) {
        return (SupplementingAnnotation) super.setID(aID);
    }

    @Override
    public SupplementingAnnotation setID(final URI aID) {
        return (SupplementingAnnotation) super.setID(aID);
    }

    @Override
    public SupplementingAnnotation setRights(final String... aRightsArray) {
        return (SupplementingAnnotation) super.setRights(aRightsArray);
    }

    @Override
    public SupplementingAnnotation setRights(final URI... aRightsArray) {
        return (SupplementingAnnotation) super.setRights(aRightsArray);
    }

    @Override
    public SupplementingAnnotation setRights(final List<URI> aRightsList) {
        return (SupplementingAnnotation) super.setRights(aRightsList);
    }

    @Override
    public SupplementingAnnotation setRequiredStatement(final RequiredStatement aStatement) {
        return (SupplementingAnnotation) super.setRequiredStatement(aStatement);
    }

    @Override
    public SupplementingAnnotation setSummary(final String aSummary) {
        return (SupplementingAnnotation) super.setSummary(aSummary);
    }

    @Override
    public SupplementingAnnotation setSummary(final Summary aSummary) {
        return (SupplementingAnnotation) super.setSummary(aSummary);
    }

    @Override
    public SupplementingAnnotation setMetadata(final Metadata aMetadata) {
        return (SupplementingAnnotation) super.setMetadata(aMetadata);
    }

    @Override
    public SupplementingAnnotation setLabel(final String aLabel) {
        return (SupplementingAnnotation) super.setLabel(aLabel);
    }

    @Override
    public SupplementingAnnotation setLabel(final Label aLabel) {
        return (SupplementingAnnotation) super.setLabel(aLabel);
    }
}
