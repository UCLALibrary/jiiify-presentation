
package info.freelibrary.iiif.presentation;

import java.net.URI;
import java.util.List;

import info.freelibrary.iiif.presentation.properties.Behavior;
import info.freelibrary.iiif.presentation.properties.Homepage;
import info.freelibrary.iiif.presentation.properties.Label;
import info.freelibrary.iiif.presentation.properties.Logo;
import info.freelibrary.iiif.presentation.properties.Metadata;
import info.freelibrary.iiif.presentation.properties.PartOf;
import info.freelibrary.iiif.presentation.properties.Rendering;
import info.freelibrary.iiif.presentation.properties.RequiredStatement;
import info.freelibrary.iiif.presentation.properties.SeeAlso;
import info.freelibrary.iiif.presentation.properties.Summary;
import info.freelibrary.iiif.presentation.properties.TimeMode;
import info.freelibrary.iiif.presentation.properties.behaviors.ResourceBehavior;
import info.freelibrary.iiif.presentation.utils.MessageCodes;
import info.freelibrary.util.Logger;
import info.freelibrary.util.LoggerFactory;

/**
 * An annotation used for painting content resources onto a {@link Canvas}.
 */
public class PaintingAnnotation extends Annotation<PaintingAnnotation> implements Resource<PaintingAnnotation>,
        ContentAnnotation<PaintingAnnotation> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaintingAnnotation.class, Constants.BUNDLE_NAME);

    private static final String MOTIVATION = "painting";

    /**
     * Creates a painting annotation.
     *
     * @param aID An ID
     * @param aCanvas A canvas
     */
    protected PaintingAnnotation(final URI aID, final Canvas aCanvas) {
        super(aID, aCanvas);
        myMotivation = MOTIVATION;
    }

    /**
     * Creates a painting annotation.
     *
     * @param aID An ID in string form
     * @param aCanvas A canvas
     */
    protected PaintingAnnotation(final String aID, final Canvas aCanvas) {
        super(aID, aCanvas);
        myMotivation = MOTIVATION;
    }

    /**
     * Creates a painting annotation.
     */
    @SuppressWarnings("unused")
    private PaintingAnnotation() {
        super();
    }

    @Override
    protected void setMotivation(final String aMotivation) {
        if (!MOTIVATION.equals(aMotivation)) {
            throw new IllegalArgumentException(LOGGER.getMessage(MessageCodes.JPA_038, MOTIVATION));
        }

        myMotivation = MOTIVATION;
    }

    @Override
    public PaintingAnnotation addBody(final ContentResource... aBody) {
        return (PaintingAnnotation) super.addBody(aBody);
    }

    @Override
    public PaintingAnnotation addBody(final List<ContentResource> aBody) {
        return addBody(aBody.toArray(new ContentResource[] {}));
    }

    @Override
    public PaintingAnnotation clearBody() {
        return (PaintingAnnotation) super.clearBody();
    }

    @Override
    public PaintingAnnotation setBody(final ContentResource... aBody) {
        return (PaintingAnnotation) super.setBody(aBody);
    }

    @Override
    public PaintingAnnotation setBody(final List<ContentResource> aBody) {
        return setBody(aBody.toArray(new ContentResource[] {}));
    }

    @Override
    public PaintingAnnotation setTarget(final URI aURI) {
        return (PaintingAnnotation) super.setTarget(aURI);
    }

    @Override
    public PaintingAnnotation setTarget(final String aURI) {
        return (PaintingAnnotation) super.setTarget(aURI);
    }

    @Override
    public PaintingAnnotation clearBehaviors() {
        return (PaintingAnnotation) super.clearBehaviors();
    }

    @Override
    public PaintingAnnotation setBehaviors(final Behavior... aBehaviorArray) {
        return (PaintingAnnotation) super.setBehaviors(aBehaviorArray);
    }

    @Override
    public PaintingAnnotation setBehaviors(final List<Behavior> aBehaviorList) {
        return (PaintingAnnotation) super.setBehaviors(aBehaviorList);
    }

    @Override
    public PaintingAnnotation addBehaviors(final Behavior... aBehaviorArray) {
        return (PaintingAnnotation) super.addBehaviors(checkBehaviors(ResourceBehavior.class, false, aBehaviorArray));
    }

    @Override
    public PaintingAnnotation addBehaviors(final List<Behavior> aBehaviorList) {
        return (PaintingAnnotation) super.addBehaviors(checkBehaviors(ResourceBehavior.class, false, aBehaviorList));
    }

    @Override
    public PaintingAnnotation setTimeMode(final TimeMode aTimeMode) {
        return (PaintingAnnotation) super.setTimeMode(aTimeMode);
    }

    @Override
    public PaintingAnnotation setSeeAlsoRefs(final SeeAlso... aSeeAlsoArray) {
        return (PaintingAnnotation) super.setSeeAlsoRefs(aSeeAlsoArray);
    }

    @Override
    public PaintingAnnotation setSeeAlsoRefs(final List<SeeAlso> aSeeAlsoList) {
        return (PaintingAnnotation) super.setSeeAlsoRefs(aSeeAlsoList);
    }

    @Override
    public PaintingAnnotation setPartOfs(final PartOf... aPartOfArray) {
        return (PaintingAnnotation) super.setPartOfs(aPartOfArray);
    }

    @Override
    public PaintingAnnotation setPartOfs(final List<PartOf> aPartOfList) {
        return (PaintingAnnotation) super.setPartOfs(aPartOfList);
    }

    @Override
    public PaintingAnnotation setRenderings(final Rendering... aRenderingArray) {
        return (PaintingAnnotation) super.setRenderings(aRenderingArray);
    }

    @Override
    public PaintingAnnotation setRenderings(final List<Rendering> aRenderingList) {
        return (PaintingAnnotation) super.setRenderings(aRenderingList);
    }

    @Override
    public PaintingAnnotation setHomepages(final Homepage... aHomepageArray) {
        return (PaintingAnnotation) super.setHomepages(aHomepageArray);
    }

    @Override
    public PaintingAnnotation setHomepages(final List<Homepage> aHomepageList) {
        return (PaintingAnnotation) super.setHomepages(aHomepageList);
    }

    @Override
    public PaintingAnnotation setThumbnails(final Thumbnail... aThumbnailArray) {
        return (PaintingAnnotation) super.setThumbnails(aThumbnailArray);
    }

    @Override
    public PaintingAnnotation setThumbnails(final List<Thumbnail> aThumbnailList) {
        return (PaintingAnnotation) super.setThumbnails(aThumbnailList);
    }

    @Override
    public PaintingAnnotation setID(final String aID) {
        return (PaintingAnnotation) super.setID(aID);
    }

    @Override
    public PaintingAnnotation setID(final URI aID) {
        return (PaintingAnnotation) super.setID(aID);
    }

    @Override
    public PaintingAnnotation setLogo(final String aLogo) {
        return (PaintingAnnotation) super.setLogo(aLogo);
    }

    @Override
    public PaintingAnnotation setLogo(final Logo aLogo) {
        return (PaintingAnnotation) super.setLogo(aLogo);
    }

    @Override
    public PaintingAnnotation setRights(final String... aRightsArray) {
        return (PaintingAnnotation) super.setRights(aRightsArray);
    }

    @Override
    public PaintingAnnotation setRights(final URI... aRightsArray) {
        return (PaintingAnnotation) super.setRights(aRightsArray);
    }

    @Override
    public PaintingAnnotation setRights(final List<URI> aRightsList) {
        return (PaintingAnnotation) super.setRights(aRightsList);
    }

    @Override
    public PaintingAnnotation setRequiredStatement(final RequiredStatement aStatement) {
        return (PaintingAnnotation) super.setRequiredStatement(aStatement);
    }

    @Override
    public PaintingAnnotation setSummary(final String aSummary) {
        return (PaintingAnnotation) super.setSummary(aSummary);
    }

    @Override
    public PaintingAnnotation setSummary(final Summary aSummary) {
        return (PaintingAnnotation) super.setSummary(aSummary);
    }

    @Override
    public PaintingAnnotation setMetadata(final Metadata aMetadata) {
        return (PaintingAnnotation) super.setMetadata(aMetadata);
    }

    @Override
    public PaintingAnnotation setLabel(final String aLabel) {
        return (PaintingAnnotation) super.setLabel(aLabel);
    }

    @Override
    public PaintingAnnotation setLabel(final Label aLabel) {
        return (PaintingAnnotation) super.setLabel(aLabel);
    }
}
