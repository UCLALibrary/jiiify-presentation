
package info.freelibrary.iiif.presentation;

import static org.junit.Assert.assertEquals;

import java.net.URI;

import org.junit.Test;

import info.freelibrary.iiif.presentation.properties.Label;
import info.freelibrary.iiif.presentation.properties.ViewingDirection;
import info.freelibrary.iiif.presentation.properties.behaviors.CanvasBehavior;
import info.freelibrary.iiif.presentation.properties.behaviors.ManifestBehavior;
import info.freelibrary.iiif.presentation.properties.behaviors.ResourceBehavior;

/**
 * Tests of {@link AnnotationCollection}.
 */
public class AnnotationCollectionTest {

    private static final URI ID = URI.create("http://example.org/id");

    private static final String LABEL = "My great label";

    /**
     * Tests an {@link AnnotationCollection} constructor.
     */
    @Test
    public void testConstructorStringIdStringLabel() {
        assertEquals(ID.toString(), new AnnotationCollection(ID.toString(), LABEL).getID().toString());
    }

    /**
     * Tests an {@link AnnotationCollection} constructor.
     */
    @Test
    public void testConstructorUriIdLabel() {
        assertEquals(ID, new AnnotationCollection(ID, new Label(LABEL)).getID());
    }

    /**
     * Tests viewing direction on a {@link AnnotationCollection}.
     */
    @Test
    public void testGetSetViewingDirection() {
        final AnnotationCollection annotationCollection = new AnnotationCollection(ID.toString(), LABEL)
                .setViewingDirection(ViewingDirection.LEFT_TO_RIGHT);
        assertEquals(ViewingDirection.LEFT_TO_RIGHT, annotationCollection.getViewingDirection());
    }

    /**
     * Test setting {@link AnnotationCollection} behaviors.
     */
    @Test
    public final void testSetBehaviors() {
        final AnnotationCollection annotationCollection = new AnnotationCollection(ID.toString(), LABEL);

        assertEquals(1, annotationCollection.setBehaviors(ResourceBehavior.HIDDEN).getBehaviors().size());
    }

    /**
     * Test setting disallowed {@link AnnotationCollection} behaviors.
     */
    @Test(expected = IllegalArgumentException.class)
    public final void testSetDisallowedBehaviors() {
        final AnnotationCollection annotationCollection = new AnnotationCollection(ID.toString(), LABEL);

        annotationCollection.setBehaviors(ManifestBehavior.AUTO_ADVANCE);
    }

    /**
     * Test adding {@link AnnotationCollection} behaviors.
     */
    @Test
    public final void testAddBehaviors() {
        final AnnotationCollection annotationCollection = new AnnotationCollection(ID.toString(), LABEL);

        assertEquals(1, annotationCollection.addBehaviors(ResourceBehavior.HIDDEN).getBehaviors().size());
    }

    /**
     * Test adding disallowed {@link AnnotationCollection} behaviors.
     */
    @Test(expected = IllegalArgumentException.class)
    public final void testAddDisallowedBehaviors() {
        final AnnotationCollection annotationCollection = new AnnotationCollection(ID.toString(), LABEL);

        annotationCollection.addBehaviors(ManifestBehavior.CONTINUOUS, CanvasBehavior.AUTO_ADVANCE);
    }

}
