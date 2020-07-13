
package info.freelibrary.iiif.presentation.v3;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import org.junit.Before;
import org.junit.Test;

import info.freelibrary.iiif.presentation.v3.properties.NavDate;
import info.freelibrary.iiif.presentation.v3.properties.behaviors.CanvasBehavior;
import info.freelibrary.iiif.presentation.v3.properties.behaviors.ManifestBehavior;
import info.freelibrary.iiif.presentation.v3.properties.selectors.MediaFragmentSelector;
import info.freelibrary.iiif.presentation.v3.services.ImageInfoService;
import info.freelibrary.iiif.presentation.v3.utils.TestUtils;
import info.freelibrary.util.StringUtils;

import io.vertx.core.json.JsonObject;

/**
 * Tests for a presentation canvas.
 */
public class CanvasTest {

    private static final String LABEL = "p. 1";

    private static final int WIDTH = 480;

    private static final int HEIGHT = 360;

    private static final int THUMBNAIL_WH = 64;

    private static final float CANVAS_DURATION = 3600;

    private static final float DURATION = 300;

    /** Identifiers */

    // Image, Sound, and Video are painting; Text is supplementing

    private static final String IMAGE_CANVAS_ID = "https://example.org/iiif/book1/page1/canvas-1";

    private static final String IMAGE_PAGE_ID =
            "https://example.org/iiif/book1/page1/annotation/painting-page-1";

    private static final String IMAGE_ANNO_ID = "https://example.org/iiif/book1/page1/annotation/painting-1";

    private static final String IMAGE_1_ID = "https://example.org/iiif/book1/page1/full/max/0/default.jpg";

    private static final String IMAGE_2_ID = "https://example.org/iiif/book1/page1/full/max/0/bitonal.jpg";

    private static final String IMAGE_THUMBNAIL_ID = "https://example.org/iiif/book1/page1/full/64,64/0/default.jpg";

    private static final String IMAGE_THUMBNAIL_SERVICE_ID = "https://example.org/iiif/book1/page1";

    private static final String SOUND_CANVAS_ID = "https://example.org/iiif/lp1/side1/track1/canvas-1";

    // Annotation and annotation page IDs for sound and video are inferred by Canvas.paintWith()

    private static final String SOUND_1_ID = "https://example.org/iiif/lp1/side1/track1.mp3";

    private static final String SOUND_2_ID = "https://example.org/iiif/lp1/side1/track1-alternate-mix.mp3";

    private static final String SOUND_3_ID = "https://example.org/iiif/lp1/side1/track2.mp3";

    private static final String VIDEO_CANVAS_ID = "https://example.org/iiif/reel1/segment1/canvas-1";

    private static final String VIDEO_1_ID = "https://example.org/iiif/reel1/segment1.mp4";

    private static final String VIDEO_2_ID = "https://example.org/iiif/reel1/segment2.mp4";

    private static final String TEXT_PAGE_ID =
            "https://example.org/iiif/book1/page1/annotation/supplementing-page-1";

    private static final String TEXT_ANNO_ID =
            "https://example.org/iiif/book1/page1/annotation/supplementing-1";

    private static final String TEXT_ID = "https://example.org/iiif/book1/page1/ocr.xml";

    /** URI media fragment component templates */

    private static final String URI_FRAGMENT_XYWH_TEMPLATE = "xywh={},{},{},{}";

    private static final String URI_FRAGMENT_T_TEMPLATE = "t={},{}";

    private static final String URI_FRAGMENT_XYWHT_TEMPLATE = "xywh={},{},{},{}&t={},{}";

    /** Test fixtures */

    private static final File CANVAS_FULL = new File(TestUtils.TEST_DIR, "canvas-full.json");

    private static final File CANVAS_IMAGE = new File(TestUtils.TEST_DIR, "canvas-image.json");

    private static final File CANVAS_IMAGE_CHOICE = new File(TestUtils.TEST_DIR, "canvas-image-choice.json");

    private static final File CANVAS_IMAGE_MULTI = new File(TestUtils.TEST_DIR, "canvas-image-multi.json");

    private static final File CANVAS_SOUND = new File(TestUtils.TEST_DIR, "canvas-sound.json");

    private static final File CANVAS_SOUND_CHOICE_MULTI = new File(TestUtils.TEST_DIR,
            "canvas-sound-choice-multi.json");

    private static final File CANVAS_VIDEO = new File(TestUtils.TEST_DIR, "canvas-video.json");

    private static final File CANVAS_VIDEO_MULTI = new File(TestUtils.TEST_DIR, "canvas-video-multi.json");

    private Canvas myCanvas;

    @Before
    public void setUp() {
        myCanvas = new Canvas(IMAGE_CANVAS_ID, LABEL);
    }

    /***********************
     * Getters and setters *
     ***********************/

    /**
     * Tests setting and getting a navDate on the canvas.
     */
    @Test
    public final void testNavDate() {
        final NavDate navDate = NavDate.now();

        assertEquals(navDate, myCanvas.setNavDate(navDate).getNavDate());
    }

    /**
     * Tests setting a canvas' width.
     */
    @Test
    public final void testGetWidth() {
        assertEquals(WIDTH, myCanvas.setWidthHeight(WIDTH, HEIGHT).getWidth());
    }

    /**
     * Tests getting a canvas' height.
     */
    @Test
    public final void testGetHeight() {
        assertEquals(HEIGHT, myCanvas.setWidthHeight(WIDTH, HEIGHT).getHeight());
    }

    /**
     * Tests getting a canvas' duration.
     */
    @Test
    public final void testGetDuration() {
        assertEquals(Float.compare(300.0f, myCanvas.setDuration(DURATION).getDuration()), 0);
    }

    /**
     * Tests setting a canvas' width to zero.
     */
    @Test(expected = IllegalArgumentException.class)
    public final void testSetZeroWidth() {
        myCanvas.setWidthHeight(0, HEIGHT);
    }

    /**
     * Tests setting a canvas' height to zero.
     */
    @Test(expected = IllegalArgumentException.class)
    public final void testSetZeroHeight() {
        myCanvas.setWidthHeight(WIDTH, 0);
    }

    /**
     * Tests setting a canvas' duration to zero.
     */
    @Test(expected = IllegalArgumentException.class)
    public final void testSetZeroDuration() {
        myCanvas.setDuration(0);
    }

    /**
     * Tests setting a canvas' duration to infinity.
     */
    @Test(expected = IllegalArgumentException.class)
    public final void testSetInfiniteDuration() {
        myCanvas.setDuration(Float.POSITIVE_INFINITY);
    }

    /**
     * Tests setting canvas behaviors.
     */
    @Test
    public final void testSetBehaviors() {
        myCanvas.setBehaviors(CanvasBehavior.FACING_PAGES, CanvasBehavior.AUTO_ADVANCE);

        assertEquals(2, myCanvas.getBehaviors().size());
    }

    /**
     * Tests setting disallowed canvas behaviors.
     */
    @Test(expected = IllegalArgumentException.class)
    public final void testSetDisallowedBehaviors() {
        myCanvas.setBehaviors(CanvasBehavior.FACING_PAGES, ManifestBehavior.AUTO_ADVANCE);
    }

    /**
     * Tests adding canvas behaviors.
     */
    @Test
    public final void testAddBehaviors() {
        assertEquals(1, myCanvas.addBehaviors(CanvasBehavior.FACING_PAGES).getBehaviors().size());
    }

    /**
     * Tests adding disallowed canvas behaviors.
     */
    @Test(expected = IllegalArgumentException.class)
    public final void testAddDisallowedBehaviors() {
        myCanvas.addBehaviors(CanvasBehavior.FACING_PAGES, ManifestBehavior.AUTO_ADVANCE);
    }

    /********************************************
     * Painting content resources onto canvases *
     ********************************************/

    /**
     * Tests painting an image onto a canvas with spatial dimensions.
     */
    @Test
    public final void testPaintImageOnSpatialCanvas() {
        final ImageContent image = new ImageContent(IMAGE_1_ID).setWidthHeight(WIDTH, HEIGHT);

        myCanvas.setWidthHeight(WIDTH, HEIGHT).paintWith(image);

        assertEquals(IMAGE_1_ID, getContentResourceID().toString());
    }

    /**
     * Tests painting an image onto a canvas with spatiotemporal dimensions.
     */
    @Test
    public final void testPaintImageOnSpatiotemporalCanvas() {
        final ImageContent image = new ImageContent(IMAGE_1_ID).setWidthHeight(WIDTH, HEIGHT);

        myCanvas.setWidthHeight(WIDTH, HEIGHT).setDuration(CANVAS_DURATION).paintWith(image);

        assertEquals(IMAGE_1_ID, getContentResourceID().toString());
    }

    /**
     * Tests painting a sound onto a canvas with temporal dimensions.
     */
    @Test
    public final void testPaintSoundOnTemporalCanvas() {
        final SoundContent sound = new SoundContent(SOUND_1_ID).setDuration(DURATION);

        myCanvas.setDuration(CANVAS_DURATION).paintWith(sound);

        assertEquals(SOUND_1_ID, getContentResourceID().toString());
    }

    /**
     * Tests painting a sound onto a canvas with spatiotemporal dimensions.
     */
    @Test
    public final void testPaintSoundOnSpatiotemporalCanvas() {
        final SoundContent sound = new SoundContent(SOUND_1_ID).setDuration(DURATION);

        myCanvas.setWidthHeight(WIDTH, HEIGHT).setDuration(CANVAS_DURATION).paintWith(sound);

        assertEquals(SOUND_1_ID, getContentResourceID().toString());
    }

    /**
     * Tests painting a video onto a canvas with spatiotemporal dimensions.
     */
    @Test
    public final void testPaintVideoOnSpatiotemporalCanvas() {
        final VideoContent video = new VideoContent(VIDEO_1_ID).setWidthHeight(WIDTH, HEIGHT).setDuration(DURATION);

        myCanvas.setWidthHeight(WIDTH, HEIGHT).setDuration(CANVAS_DURATION).paintWith(video);

        assertEquals(VIDEO_1_ID, getContentResourceID().toString());
    }

    /****************************************************************
     * Painting content resources of unspecified size onto canvases *
     ****************************************************************/

    /**
     * Tests painting an image of unspecified size onto a canvas with spatial dimensions.
     */
    @Test
    public final void testPaintImageOnSpatialCanvasNoDims() {
        final ImageContent image = new ImageContent(IMAGE_1_ID);

        myCanvas.setWidthHeight(WIDTH, HEIGHT).paintWith(image);

        assertEquals(IMAGE_1_ID, getContentResourceID().toString());
    }

    /**
     * Tests painting an image of unspecified size onto a canvas with spatiotemporal dimensions.
     */
    @Test
    public final void testPaintImageOnSpatiotemporalCanvasNoDims() {
        final ImageContent image = new ImageContent(IMAGE_1_ID);

        myCanvas.setWidthHeight(WIDTH, HEIGHT).setDuration(CANVAS_DURATION).paintWith(image);

        assertEquals(IMAGE_1_ID, getContentResourceID().toString());
    }

    /**
     * Tests painting a sound of unspecified size onto a canvas with temporal dimensions.
     */
    @Test
    public final void testPaintSoundOnTemporalCanvasNoDims() {
        final SoundContent sound = new SoundContent(SOUND_1_ID);

        myCanvas.setDuration(CANVAS_DURATION).paintWith(sound);

        assertEquals(SOUND_1_ID, getContentResourceID().toString());
    }

    /**
     * Tests painting a sound of unspecified size onto a canvas with spatiotemporal dimensions.
     */
    @Test
    public final void testPaintSoundOnSpatiotemporalCanvasNoDims() {
        final SoundContent sound = new SoundContent(SOUND_1_ID);

        myCanvas.setWidthHeight(WIDTH, HEIGHT).setDuration(CANVAS_DURATION).paintWith(sound);

        assertEquals(SOUND_1_ID, getContentResourceID().toString());
    }

    /**
     * Tests painting a video of unspecified size onto a canvas with spatiotemporal dimensions.
     */
    @Test
    public final void testPaintVideoOnSpatiotemporalCanvasNoDims() {
        final VideoContent video = new VideoContent(VIDEO_1_ID);

        myCanvas.setWidthHeight(WIDTH, HEIGHT).setDuration(CANVAS_DURATION).paintWith(video);

        assertEquals(VIDEO_1_ID, getContentResourceID().toString());
    }

    /*********************************************************
     * Content resource has dimensions which canvas does not *
     *********************************************************/

    /**
     * Tests painting an image onto a canvas with temporal dimensions.
     */
    @Test(expected = ContentOutOfBoundsException.class)
    public final void testPaintImageOnTemoporalCanvas() {
        final ImageContent image = new ImageContent(IMAGE_1_ID);

        myCanvas.setDuration(CANVAS_DURATION).paintWith(image);
    }

    /**
     * Tests painting a sound onto a canvas with spatial dimensions.
     */
    @Test(expected = ContentOutOfBoundsException.class)
    public final void testPaintSoundOnSpatialCanvas() {
        final SoundContent sound = new SoundContent(SOUND_1_ID);

        myCanvas.setWidthHeight(WIDTH, HEIGHT).paintWith(sound);
    }

    /**
     * Tests painting a video onto a canvas with spatial dimensions.
     */
    @Test(expected = ContentOutOfBoundsException.class)
    public final void testPaintVideoOnSpatialCanvas() {
        final VideoContent video = new VideoContent(VIDEO_1_ID);

        myCanvas.setWidthHeight(WIDTH, HEIGHT).paintWith(video);
    }

    /**
     * Tests painting a video onto a canvas with temporal dimensions.
     */
    @Test(expected = ContentOutOfBoundsException.class)
    public final void testPaintVideoOnTemoporalCanvas() {
        final VideoContent video = new VideoContent(VIDEO_1_ID);

        myCanvas.setDuration(CANVAS_DURATION).paintWith(video);
    }

    /******************************************
     * Content resource is too big for canvas *
     ******************************************/

    /**
     * Tests painting an image outside the bounds of a canvas with spatial dimensions.
     */
    @Test(expected = ContentOutOfBoundsException.class)
    public final void testPaintImageOnSpatialCanvasOutOfBounds() {
        final ImageContent image = new ImageContent(IMAGE_1_ID).setWidthHeight(WIDTH, HEIGHT);

        myCanvas.setWidthHeight(WIDTH - 1, HEIGHT).paintWith(image);
    }

    /**
     * Tests painting a sound outside the bounds of a canvas with temporal dimensions.
     */
    @Test(expected = ContentOutOfBoundsException.class)
    public final void testPaintSoundOnTemporalCanvasOutOfBounds() {
        final SoundContent sound = new SoundContent(SOUND_1_ID).setDuration(DURATION);

        myCanvas.setDuration(DURATION - 1).paintWith(sound);
    }

    /**
     * Tests painting a video outside the spatial bounds of a canvas with spatiotemporal dimensions.
     */
    @Test(expected = ContentOutOfBoundsException.class)
    public final void testPaintVideoOnSpatiotemporalCanvasOutOfBoundsSpatial() {
        final VideoContent video = new VideoContent(VIDEO_1_ID).setWidthHeight(WIDTH, HEIGHT).setDuration(DURATION);

        myCanvas.setWidthHeight(WIDTH, HEIGHT - 1).setDuration(CANVAS_DURATION).paintWith(video);
    }

    /**
     * Tests painting a video outside the temporal bounds of a canvas with spatiotemporal dimensions.
     */
    @Test(expected = ContentOutOfBoundsException.class)
    public final void testPaintVideoOnSpatiotemporalCanvasOutOfBoundsTemporal() {
        final VideoContent video = new VideoContent(VIDEO_1_ID).setWidthHeight(WIDTH, HEIGHT).setDuration(DURATION);

        myCanvas.setWidthHeight(WIDTH, HEIGHT).setDuration(DURATION - 1).paintWith(video);
    }

    /****************************************************
     * Painting content resources onto canvas fragments *
     ****************************************************/

    /**
     * Tests painting an image onto a spatial fragment of a canvas with spatial dimensions.
     */
    @Test
    public final void testPaintImageOnSpatialFragmentOfSpatialCanvas() {
        final ImageContent image = new ImageContent(IMAGE_1_ID).setWidthHeight(WIDTH, HEIGHT);
        final MediaFragmentSelector selector = new MediaFragmentSelector(0, 0, WIDTH, HEIGHT);

        myCanvas.setWidthHeight(WIDTH, HEIGHT).paintWith(selector, image);

        assertEquals(IMAGE_1_ID, getContentResourceID().toString());
        assertEquals(selector.getValue(), getMediaFragment());
    }

    /**
     * Tests painting an image onto a spatial fragment of a canvas with spatiotemporal dimensions.
     */
    @Test
    public final void testPaintImageOnSpatialFragmentOfSpatiotemporalCanvas() {
        final ImageContent image = new ImageContent(IMAGE_1_ID).setWidthHeight(WIDTH, HEIGHT);
        final MediaFragmentSelector selector = new MediaFragmentSelector(0, 0, WIDTH, HEIGHT);

        myCanvas.setWidthHeight(WIDTH, HEIGHT).setDuration(CANVAS_DURATION).paintWith(selector, image);

        assertEquals(IMAGE_1_ID, getContentResourceID().toString());
        assertEquals(selector.getValue(), getMediaFragment().toString());
    }

    /**
     * Tests painting an image onto a temporal fragment of a canvas with spatiotemporal dimensions.
     */
    @Test
    public final void testPaintImageOnTemporalFragmentOfSpatiotemporalCanvas() {
        final ImageContent image = new ImageContent(IMAGE_1_ID).setWidthHeight(WIDTH, HEIGHT);
        final MediaFragmentSelector selector = new MediaFragmentSelector(0.0f, DURATION);

        myCanvas.setWidthHeight(WIDTH, HEIGHT).setDuration(CANVAS_DURATION).paintWith(selector, image);

        assertEquals(IMAGE_1_ID, getContentResourceID().toString());
        assertEquals(selector.getValue(), getMediaFragment());
    }

    /**
     * Tests painting an image onto a spatiotemporal fragment of a canvas with spatiotemporal dimensions.
     */
    @Test
    public final void testPaintImageOnSpatiotemporalFragmentOfSpatiotemporalCanvas() {
        final ImageContent image = new ImageContent(IMAGE_1_ID).setWidthHeight(WIDTH, HEIGHT);
        final MediaFragmentSelector selector = new MediaFragmentSelector(0, 0, WIDTH, HEIGHT, 0.0f, DURATION);

        myCanvas.setWidthHeight(WIDTH, HEIGHT).setDuration(CANVAS_DURATION).paintWith(selector, image);

        assertEquals(IMAGE_1_ID, getContentResourceID().toString());
        assertEquals(selector.getValue(), getMediaFragment());
    }

    /**
     * Tests painting a sound onto a temporal fragment of a canvas with temporal dimensions.
     */
    @Test
    public final void testPaintSoundOnTemporalFragmentOfTemporalCanvas() {
        final SoundContent sound = new SoundContent(SOUND_1_ID).setDuration(DURATION);
        final MediaFragmentSelector selector = new MediaFragmentSelector(0.0f, DURATION);

        myCanvas.setDuration(CANVAS_DURATION).paintWith(selector, sound);

        assertEquals(SOUND_1_ID, getContentResourceID().toString());
        assertEquals(selector.getValue(), getMediaFragment());
    }

    /**
     * Tests painting a sound onto a spatial fragment of a canvas with spatiotemporal dimensions.
     */
    @Test
    public final void testPaintSoundOnSpatialFragmentOfSpatiotemporalCanvas() {
        final String foundMediaFragment;
        final SoundContent sound = new SoundContent(SOUND_1_ID).setDuration(DURATION);
        final MediaFragmentSelector selector = new MediaFragmentSelector(0, 0, WIDTH, HEIGHT);

        myCanvas.setWidthHeight(WIDTH, HEIGHT).setDuration(CANVAS_DURATION).paintWith(selector, sound);

        foundMediaFragment = ((MediaFragmentSelector) ((SpecificResource) myCanvas.getPaintingPages().get(0)
                .getAnnotations().get(0).getTarget()).getSelector()).getValue();

        assertEquals(SOUND_1_ID, getContentResourceID().toString());
        assertEquals(selector.getValue(), foundMediaFragment);
    }

    /**
     * Tests painting a sound onto a temporal fragment of a canvas with spatiotemporal dimensions.
     */
    @Test
    public final void testPaintSoundOnTemporalFragmentOfSpatiotemporalCanvas() {
        final SoundContent sound = new SoundContent(SOUND_1_ID).setDuration(DURATION);
        final MediaFragmentSelector selector = new MediaFragmentSelector(0.0f, DURATION);

        myCanvas.setWidthHeight(WIDTH, HEIGHT).setDuration(CANVAS_DURATION).paintWith(selector, sound);

        assertEquals(SOUND_1_ID, getContentResourceID().toString());
        assertEquals(selector.getValue(), getMediaFragment());
    }

    /**
     * Tests painting a sound onto a spatiotemporal fragment of a canvas with spatiotemporal dimensions.
     */
    @Test
    public final void testPaintSoundOnSpatiotemporalFragmentOfSpatiotemporalCanvas() {
        final SoundContent sound = new SoundContent(SOUND_1_ID).setDuration(DURATION);
        final MediaFragmentSelector selector = new MediaFragmentSelector(0, 0, WIDTH, HEIGHT, 0.0f, DURATION);

        myCanvas.setWidthHeight(WIDTH, HEIGHT).setDuration(CANVAS_DURATION).paintWith(selector, sound);

        assertEquals(SOUND_1_ID, getContentResourceID().toString());
        assertEquals(selector.getValue(), getMediaFragment());
    }

    /**
     * Tests painting a video onto a spatial fragment of a canvas with spatiotemporal dimensions.
     */
    @Test
    public final void testPaintVideoOnSpatialFragmentOfSpatiotemporalCanvas() {
        final VideoContent video = new VideoContent(VIDEO_1_ID).setWidthHeight(WIDTH, HEIGHT).setDuration(DURATION);
        final MediaFragmentSelector selector = new MediaFragmentSelector(0, 0, WIDTH, HEIGHT);

        myCanvas.setWidthHeight(WIDTH, HEIGHT).setDuration(CANVAS_DURATION).paintWith(selector, video);

        assertEquals(VIDEO_1_ID, getContentResourceID().toString());
        assertEquals(selector.getValue(), getMediaFragment());
    }

    /**
     * Tests painting a video onto a temporal fragment of a canvas with spatiotemporal dimensions.
     */
    @Test
    public final void testPaintVideoOnTemporalFragmentOfSpatiotemporalCanvas() {
        final VideoContent video = new VideoContent(VIDEO_1_ID).setWidthHeight(WIDTH, HEIGHT).setDuration(DURATION);
        final MediaFragmentSelector selector = new MediaFragmentSelector(0.0f, CANVAS_DURATION);

        myCanvas.setWidthHeight(WIDTH, HEIGHT).setDuration(CANVAS_DURATION).paintWith(selector, video);

        assertEquals(VIDEO_1_ID, getContentResourceID().toString());
        assertEquals(selector.getValue(), getMediaFragment());
    }

    /**
     * Tests painting a video onto a spatiotemporal fragment of a canvas with spatiotemporal dimensions.
     */
    @Test
    public final void testPaintVideoOnSpatiotemporalFragmentOfSpatiotemporalCanvas() {
        final VideoContent video = new VideoContent(VIDEO_1_ID).setWidthHeight(WIDTH, HEIGHT).setDuration(DURATION);
        final MediaFragmentSelector selector = new MediaFragmentSelector(0, 0, WIDTH, HEIGHT, 0.0f, CANVAS_DURATION);

        myCanvas.setWidthHeight(WIDTH, HEIGHT).setDuration(CANVAS_DURATION).paintWith(selector, video);

        assertEquals(VIDEO_1_ID, getContentResourceID().toString());
        assertEquals(selector.getValue(), getMediaFragment());
    }

    /************************************************************************
     * Painting content resources of unspecified size onto canvas fragments *
     ************************************************************************/

    /**
     * Tests painting an image of unspecified size onto a spatial fragment of a canvas with spatial dimensions.
     */
    @Test
    public final void testPaintImageOnSpatialFragmentOfSpatialCanvasNoDims() {
        final ImageContent image = new ImageContent(IMAGE_1_ID);
        final MediaFragmentSelector selector = new MediaFragmentSelector(WIDTH / 2, HEIGHT / 2, WIDTH / 2, HEIGHT / 2);

        myCanvas.setWidthHeight(WIDTH, HEIGHT).paintWith(selector, image);

        assertEquals(IMAGE_1_ID, getContentResourceID().toString());
        assertEquals(selector.getValue(), getMediaFragment());
    }

    /**
     * Tests painting a sound of unspecified size onto a temporal fragment of a canvas with temporal dimensions.
     */
    @Test
    public final void testPaintSoundOnTemporalFragmentOfTemporalCanvasNoDims() {
        final SoundContent sound = new SoundContent(SOUND_1_ID);
        final MediaFragmentSelector selector = new MediaFragmentSelector(DURATION, 1.5f * DURATION);

        myCanvas.setDuration(CANVAS_DURATION).paintWith(selector, sound);

        assertEquals(SOUND_1_ID, getContentResourceID().toString());
        assertEquals(selector.getValue(), getMediaFragment());
    }

    /**
     * Tests painting a video of unspecified size onto a spatiotemporal fragment of a canvas with spatiotemporal
     * dimensions.
     */
    @Test
    public final void testPaintVideoOnSpatiotemporalFragmentOfSpatiotemporalCanvasNoDims() {
        final VideoContent video = new VideoContent(VIDEO_1_ID);
        final MediaFragmentSelector selector = new MediaFragmentSelector(0, 0, WIDTH, HEIGHT,
                CANVAS_DURATION - DURATION / 2);

        myCanvas.setWidthHeight(WIDTH, HEIGHT).setDuration(CANVAS_DURATION).paintWith(selector, video);

        assertEquals(VIDEO_1_ID, getContentResourceID().toString());
        assertEquals(selector.getValue(), getMediaFragment());
    }

    /********************************************************
     * Canvas fragment has dimensions which canvas does not *
     ********************************************************/

    /**
     * Tests painting an image onto a non-existent temporal fragment of a canvas with spatial dimensions.
     */
    @Test(expected = SelectorOutOfBoundsException.class)
    public final void testPaintImageOnUndefinedTemporalFragmentOfSpatialCanvas() {
        final ImageContent image = new ImageContent(IMAGE_1_ID).setWidthHeight(WIDTH, HEIGHT);
        final MediaFragmentSelector selector = new MediaFragmentSelector(0.0f, DURATION);

        myCanvas.setWidthHeight(WIDTH, HEIGHT).paintWith(selector, image);
    }

    /**
     * Tests painting an image onto a non-existent spatial fragment of a canvas with temporal dimensions.
     */
    @Test(expected = SelectorOutOfBoundsException.class)
    public final void testPaintImageOnUndefinedSpatialFragmentOfTemporalCanvas() {
        final ImageContent image = new ImageContent(IMAGE_1_ID).setWidthHeight(WIDTH, HEIGHT);
        final MediaFragmentSelector selector = new MediaFragmentSelector(0, 0, WIDTH, HEIGHT);

        myCanvas.setDuration(CANVAS_DURATION).paintWith(selector, image);
    }

    /**
     * Tests painting an image onto a non-existent spatiotemporal fragment of a canvas with spatial dimensions.
     */
    @Test(expected = SelectorOutOfBoundsException.class)
    public final void testPaintImageOnUndefinedSpatiotemporalFragmentOfSpatialCanvas() {
        final ImageContent image = new ImageContent(IMAGE_1_ID).setWidthHeight(WIDTH, HEIGHT);
        final MediaFragmentSelector selector = new MediaFragmentSelector(0, 0, WIDTH, HEIGHT, 0.0f, DURATION);

        myCanvas.setWidthHeight(WIDTH, HEIGHT).paintWith(selector, image);
    }

    /**
     * Tests painting an image onto a non-existent spatiotemporal fragment of a canvas with temporal dimensions.
     */
    @Test(expected = SelectorOutOfBoundsException.class)
    public final void testPaintImageOnUndefinedSpatiotemporalFragmentOfTemporalCanvas() {
        final ImageContent image = new ImageContent(IMAGE_1_ID).setWidthHeight(WIDTH, HEIGHT);
        final MediaFragmentSelector selector = new MediaFragmentSelector(0, 0, WIDTH, HEIGHT, 0.0f, DURATION);

        myCanvas.setDuration(CANVAS_DURATION).paintWith(selector, image);
    }

    /**
     * Tests painting a sound onto a non-existent temporal fragment of a canvas with spatial dimensions.
     */
    @Test(expected = SelectorOutOfBoundsException.class)
    public final void testPaintSoundOnUndefinedTemporalFragmentOfSpatialCanvas() {
        final SoundContent sound = new SoundContent(SOUND_1_ID).setDuration(DURATION);
        final MediaFragmentSelector selector = new MediaFragmentSelector(0.0f, DURATION);

        myCanvas.setWidthHeight(WIDTH, HEIGHT).paintWith(selector, sound);
    }

    /**
     * Tests painting a sound onto a non-existent spatial fragment of a canvas with temporal dimensions.
     */
    @Test(expected = SelectorOutOfBoundsException.class)
    public final void testPaintSoundOnUndefinedSpatialFragmentOfTemporalCanvas() {
        final SoundContent sound = new SoundContent(SOUND_1_ID).setDuration(DURATION);
        final MediaFragmentSelector selector = new MediaFragmentSelector(0, 0, WIDTH, HEIGHT);

        myCanvas.setDuration(CANVAS_DURATION).paintWith(selector, sound);
    }

    /**
     * Tests painting a sound onto a non-existent spatiotemporal fragment of a canvas with spatial dimensions.
     */
    @Test(expected = SelectorOutOfBoundsException.class)
    public final void testPaintSoundOnUndefinedSpatiotemporalFragmentOfSpatialCanvas() {
        final SoundContent sound = new SoundContent(SOUND_1_ID).setDuration(DURATION);
        final MediaFragmentSelector selector = new MediaFragmentSelector(0, 0, WIDTH, HEIGHT, 0.0f, DURATION);

        myCanvas.setWidthHeight(WIDTH, HEIGHT).paintWith(selector, sound);
    }

    /**
     * Tests painting a sound onto a non-existent spatiotemporal fragment of a canvas with temporal dimensions.
     */
    @Test(expected = SelectorOutOfBoundsException.class)
    public final void testPaintSoundOnUndefinedSpatiotemporalFragmentOfTemporalCanvas() {
        final SoundContent sound = new SoundContent(SOUND_1_ID).setDuration(DURATION);
        final MediaFragmentSelector selector = new MediaFragmentSelector(0, 0, WIDTH, HEIGHT, 0.0f, DURATION);

        myCanvas.setDuration(CANVAS_DURATION).paintWith(selector, sound);
    }

    /**
     * Tests painting a video onto a non-existent temporal fragment of a canvas with spatial dimensions.
     */
    @Test(expected = SelectorOutOfBoundsException.class)
    public final void testPaintVideoOnUndefinedTemporalFragmentOfSpatialCanvas() {
        final VideoContent video = new VideoContent(VIDEO_1_ID).setWidthHeight(WIDTH, HEIGHT).setDuration(DURATION);
        final MediaFragmentSelector selector = new MediaFragmentSelector(0.0f, DURATION);

        myCanvas.setWidthHeight(WIDTH, HEIGHT).paintWith(selector, video);
    }

    /**
     * Tests painting a video onto a non-existent spatial fragment of a canvas with temporal dimensions.
     */
    @Test(expected = SelectorOutOfBoundsException.class)
    public final void testPaintVideoOnUndefinedSpatialFragmentOfTemporalCanvas() {
        final VideoContent video = new VideoContent(VIDEO_1_ID).setWidthHeight(WIDTH, HEIGHT).setDuration(DURATION);
        final MediaFragmentSelector selector = new MediaFragmentSelector(0, 0, WIDTH, HEIGHT);

        myCanvas.setDuration(CANVAS_DURATION).paintWith(selector, video);
    }

    /**
     * Tests painting a video onto a non-existent spatiotemporal fragment of a canvas with spatial dimensions.
     */
    @Test(expected = SelectorOutOfBoundsException.class)
    public final void testPaintVideoOnUndefinedSpatiotemporalFragmentOfSpatialCanvas() {
        final VideoContent video = new VideoContent(VIDEO_1_ID).setWidthHeight(WIDTH, HEIGHT).setDuration(DURATION);
        final MediaFragmentSelector selector = new MediaFragmentSelector(0, 0, WIDTH, HEIGHT, 0.0f, DURATION);

        myCanvas.setWidthHeight(WIDTH, HEIGHT).paintWith(selector, video);
    }

    /**
     * Tests painting a video onto a non-existent spatiotemporal fragment of a canvas with temporal dimensions.
     */
    @Test(expected = SelectorOutOfBoundsException.class)
    public final void testPaintVideoOnUndefinedSpatiotemporalFragmentOfTemporalCanvas() {
        final VideoContent video = new VideoContent(VIDEO_1_ID).setWidthHeight(WIDTH, HEIGHT).setDuration(DURATION);
        final MediaFragmentSelector selector = new MediaFragmentSelector(0, 0, WIDTH, HEIGHT, 0.0f, DURATION);

        myCanvas.setDuration(CANVAS_DURATION).paintWith(selector, video);
    }

    /*******************************************
     * Canvas does not contain canvas fragment *
     *******************************************/

    /**
     * Tests painting an image onto a non-existent spatial fragment of a canvas with spatial dimensions.
     */
    @Test(expected = SelectorOutOfBoundsException.class)
    public final void testPaintImageOnUndefinedSpatialFragmentOfSpatialCanvas() {
        final ImageContent image = new ImageContent(IMAGE_1_ID).setWidthHeight(WIDTH, HEIGHT);
        final MediaFragmentSelector selector = new MediaFragmentSelector(0, 0, WIDTH, HEIGHT + 1);

        myCanvas.setWidthHeight(WIDTH, HEIGHT).paintWith(selector, image);
    }

    /**
     * Tests painting an image onto a non-existent spatial fragment of a canvas with spatiotemporal dimensions.
     */
    @Test(expected = SelectorOutOfBoundsException.class)
    public final void testPaintImageOnUndefinedSpatialFragmentOfSpatiotemporalCanvas() {
        final ImageContent image = new ImageContent(IMAGE_1_ID).setWidthHeight(WIDTH, HEIGHT);
        final MediaFragmentSelector selector = new MediaFragmentSelector(0, 0, WIDTH, HEIGHT + 1);

        myCanvas.setWidthHeight(WIDTH, HEIGHT).setDuration(CANVAS_DURATION).paintWith(selector, image);
    }

    /**
     * Tests painting an image onto a non-existent temporal fragment of a canvas with spatiotemporal dimensions.
     */
    @Test(expected = SelectorOutOfBoundsException.class)
    public final void testPaintImageOnUndefinedTemporalFragmentOfSpatiotemporalCanvas() {
        final ImageContent image = new ImageContent(IMAGE_1_ID).setWidthHeight(WIDTH, HEIGHT);
        final MediaFragmentSelector selector = new MediaFragmentSelector(0.0f, CANVAS_DURATION + 1);

        myCanvas.setWidthHeight(WIDTH, HEIGHT).setDuration(CANVAS_DURATION).paintWith(selector, image);
    }

    /**
     * Tests painting an image onto a non-existent spatiotemporal fragment of a canvas with spatiotemporal dimensions.
     */
    @Test(expected = SelectorOutOfBoundsException.class)
    public final void testPaintImageOnUndefinedSpatiotemporalFragmentOfSpatiotemporalCanvas() {
        final ImageContent image = new ImageContent(IMAGE_1_ID).setWidthHeight(WIDTH, HEIGHT);
        final MediaFragmentSelector selector = new MediaFragmentSelector(0, 0, WIDTH, HEIGHT + 1, 0.0f,
                CANVAS_DURATION + 1);

        myCanvas.setWidthHeight(WIDTH, HEIGHT).setDuration(CANVAS_DURATION).paintWith(selector, image);
    }

    /**
     * Tests painting a sound onto a non-existent temporal fragment of a canvas with temporal dimensions.
     */
    @Test(expected = SelectorOutOfBoundsException.class)
    public final void testPaintSoundOnUndefinedTemporalFragmentOfTemporalCanvas() {
        final SoundContent sound = new SoundContent(SOUND_1_ID).setDuration(DURATION);
        final MediaFragmentSelector selector = new MediaFragmentSelector(CANVAS_DURATION, CANVAS_DURATION + DURATION);

        myCanvas.setDuration(CANVAS_DURATION).paintWith(selector, sound);
    }

    /**
     * Tests painting a sound onto a non-existent spatial fragment of a canvas with spatiotemporal dimensions.
     */
    @Test(expected = SelectorOutOfBoundsException.class)
    public final void testPaintSoundOnUndefinedSpatialFragmentOfSpatiotemporalCanvas() {
        final SoundContent sound = new SoundContent(SOUND_1_ID).setDuration(DURATION);
        final MediaFragmentSelector selector = new MediaFragmentSelector(WIDTH, HEIGHT, WIDTH + WIDTH, HEIGHT + HEIGHT);

        myCanvas.setWidthHeight(WIDTH, HEIGHT).setDuration(CANVAS_DURATION).paintWith(selector, sound);
    }

    /**
     * Tests painting a sound onto a non-existent temporal fragment of a canvas with spatiotemporal dimensions.
     */
    @Test(expected = SelectorOutOfBoundsException.class)
    public final void testPaintSoundOnUndefinedTemporalFragmentOfSpatiotemporalCanvas() {
        final SoundContent sound = new SoundContent(SOUND_1_ID).setDuration(DURATION);
        final MediaFragmentSelector selector = new MediaFragmentSelector(CANVAS_DURATION / 2,
                3.0f / 2 * CANVAS_DURATION);

        myCanvas.setWidthHeight(WIDTH, HEIGHT).setDuration(CANVAS_DURATION).paintWith(selector, sound);
    }

    /**
     * Tests painting a sound onto a non-existent spatiotemporal fragment of a canvas with spatiotemporal dimensions.
     */
    @Test(expected = SelectorOutOfBoundsException.class)
    public final void testPaintSoundOnUndefinedSpatiotemporalFragmentOfSpatiotemporalCanvas() {
        final SoundContent sound = new SoundContent(SOUND_1_ID).setDuration(DURATION);
        final MediaFragmentSelector selector = new MediaFragmentSelector(0, 0, WIDTH + 1, HEIGHT, 0.0f,
                CANVAS_DURATION);

        myCanvas.setWidthHeight(WIDTH, HEIGHT).setDuration(CANVAS_DURATION).paintWith(selector, sound);
    }

    /**
     * Tests painting a video onto a non-existent spatial fragment of a canvas with spatiotemporal dimensions.
     */
    @Test(expected = SelectorOutOfBoundsException.class)
    public final void testPaintVideoOnUndefinedSpatialFragmentOfSpatiotemporalCanvas() {
        final VideoContent video = new VideoContent(VIDEO_1_ID).setWidthHeight(WIDTH, HEIGHT).setDuration(DURATION);
        final MediaFragmentSelector selector = new MediaFragmentSelector(0, 0, WIDTH + 1, HEIGHT);

        myCanvas.setWidthHeight(WIDTH, HEIGHT).setDuration(CANVAS_DURATION).paintWith(selector, video);
    }

    /**
     * Tests painting a video onto a non-existent temporal fragment of a canvas with spatiotemporal dimensions.
     */
    @Test(expected = SelectorOutOfBoundsException.class)
    public final void testPaintVideoOnUndefinedTemporalFragmentOfSpatiotemporalCanvas() {
        final VideoContent video = new VideoContent(VIDEO_1_ID).setWidthHeight(WIDTH, HEIGHT).setDuration(DURATION);
        final MediaFragmentSelector selector = new MediaFragmentSelector(0.0f, CANVAS_DURATION + 1);

        myCanvas.setWidthHeight(WIDTH, HEIGHT).setDuration(CANVAS_DURATION).paintWith(selector, video);
    }

    /**
     * Tests painting a video onto a non-existent spatiotemporal fragment of a canvas with spatiotemporal dimensions.
     */
    @Test(expected = SelectorOutOfBoundsException.class)
    public final void testPaintVideoOnUndefinedSpatiotemporalFragmentOfSpatiotemporalCanvas() {
        final VideoContent video = new VideoContent(VIDEO_1_ID).setWidthHeight(WIDTH, HEIGHT).setDuration(DURATION);
        final MediaFragmentSelector selector = new MediaFragmentSelector(0, 0, WIDTH + 1, HEIGHT, 0.0f,
                CANVAS_DURATION);

        myCanvas.setWidthHeight(WIDTH, HEIGHT).setDuration(CANVAS_DURATION).paintWith(selector, video);
    }

    /******************************************************************
     * Content resource has dimensions which canvas fragment does not *
     ******************************************************************/

    /**
     * Tests painting an image onto a temporal fragment of a canvas with temporal dimensions.
     */
    @Test(expected = ContentOutOfBoundsException.class)
    public final void testPaintImageOnTemporalFragmentOfTemporalCanvas() {
        final ImageContent image = new ImageContent(IMAGE_1_ID).setWidthHeight(WIDTH, HEIGHT);
        final MediaFragmentSelector selector = new MediaFragmentSelector(0.0f, DURATION);

        myCanvas.setDuration(CANVAS_DURATION).paintWith(selector, image);
    }

    /**
     * Tests painting a sound onto a spatial fragment of a canvas with spatial dimensions.
     */
    @Test(expected = ContentOutOfBoundsException.class)
    public final void testPaintSoundOnSpatialFragmentOfSpatialCanvas() {
        final SoundContent sound = new SoundContent(SOUND_1_ID).setDuration(DURATION);
        final MediaFragmentSelector selector = new MediaFragmentSelector(0, 0, WIDTH, HEIGHT);

        myCanvas.setWidthHeight(WIDTH, HEIGHT).paintWith(selector, sound);
    }

    /**
     * Tests painting a video onto a spatial fragment of a canvas with spatial dimensions.
     */
    @Test(expected = ContentOutOfBoundsException.class)
    public final void testPaintVideoOnSpatialFragmentOfSpatialCanvas() {
        final VideoContent video = new VideoContent(VIDEO_1_ID).setWidthHeight(WIDTH, HEIGHT).setDuration(DURATION);
        final MediaFragmentSelector selector = new MediaFragmentSelector(0, 0, WIDTH, HEIGHT);

        myCanvas.setWidthHeight(WIDTH, HEIGHT).paintWith(selector, video);
    }

    /**
     * Tests painting a video onto a temporal fragment of a canvas with temporal dimensions.
     */
    @Test(expected = ContentOutOfBoundsException.class)
    public final void testPaintVideoOnTemporalFragmentOfTemporalCanvas() {
        final VideoContent video = new VideoContent(VIDEO_1_ID).setWidthHeight(WIDTH, HEIGHT).setDuration(DURATION);
        final MediaFragmentSelector selector = new MediaFragmentSelector(0.0f, DURATION);

        myCanvas.setDuration(CANVAS_DURATION).paintWith(selector, video);
    }

    /***************************************************
     * Content resource is too big for canvas fragment *
     ***************************************************/

    /**
     * Tests painting an image outside the bounds of a spatial fragment of a canvas with spatial dimensions.
     */
    @Test(expected = ContentOutOfBoundsException.class)
    public final void testPaintImageOnSpatialFragmentOfSpatialCanvasOutOfBounds() {
        final ImageContent image = new ImageContent(IMAGE_1_ID).setWidthHeight(WIDTH, HEIGHT);
        final MediaFragmentSelector selector = new MediaFragmentSelector(0, 0, WIDTH - 1, HEIGHT);

        myCanvas.setWidthHeight(WIDTH, HEIGHT).paintWith(selector, image);
    }

    /**
     * Tests painting an image outside the bounds of a spatial fragment of a canvas with spatiotemporal dimensions.
     */
    @Test(expected = ContentOutOfBoundsException.class)
    public final void testPaintImageOnSpatialFragmentOfSpatiotemporalCanvasOutOfBounds() {
        final ImageContent image = new ImageContent(IMAGE_1_ID).setWidthHeight(WIDTH, HEIGHT);
        final MediaFragmentSelector selector = new MediaFragmentSelector(0, 0, WIDTH, HEIGHT - 1);

        myCanvas.setWidthHeight(WIDTH, HEIGHT).setDuration(CANVAS_DURATION).paintWith(selector, image);
    }

    /**
     * Tests painting an image outside the bounds of a temporal fragment of a canvas with spatiotemporal dimensions.
     */
    @Test(expected = ContentOutOfBoundsException.class)
    public final void testPaintImageOnTemporalFragmentOfSpatiotemporalCanvasOutOfBounds() {
        final ImageContent image = new ImageContent(IMAGE_1_ID).setWidthHeight(WIDTH + 1, HEIGHT);
        final MediaFragmentSelector selector = new MediaFragmentSelector(0.0f, CANVAS_DURATION);

        myCanvas.setWidthHeight(WIDTH, HEIGHT).setDuration(CANVAS_DURATION).paintWith(selector, image);
    }

    /**
     * Tests painting an image outside the bounds of a spatiotemporal fragment of a canvas with spatiotemporal
     * dimensions.
     */
    @Test(expected = ContentOutOfBoundsException.class)
    public final void testPaintImageOnSpatiotemporalFragmentOfSpatiotemporalCanvasOutOfBounds() {
        final ImageContent image = new ImageContent(IMAGE_1_ID).setWidthHeight(WIDTH + 1, HEIGHT);
        final MediaFragmentSelector selector = new MediaFragmentSelector(0, 0, WIDTH, HEIGHT, 0.0f, CANVAS_DURATION);

        myCanvas.setWidthHeight(WIDTH, HEIGHT).setDuration(CANVAS_DURATION).paintWith(selector, image);
    }

    /**
     * Tests painting a sound outside the bounds of a temporal fragment of a canvas with temporal dimensions.
     */
    @Test(expected = ContentOutOfBoundsException.class)
    public final void testPaintSoundOnTemporalFragmentOfTemporalCanvasOutOfBounds() {
        final SoundContent sound = new SoundContent(SOUND_1_ID).setDuration(DURATION);
        final MediaFragmentSelector selector = new MediaFragmentSelector(0.0f, DURATION - 1);

        myCanvas.setDuration(CANVAS_DURATION).paintWith(selector, sound);
    }

    /**
     * Tests painting a sound outside the bounds of a spatial fragment of a canvas with spatiotemporal dimensions.
     */
    @Test(expected = ContentOutOfBoundsException.class)
    public final void testPaintSoundOnSpatialFragmentOfSpatiotemporalCanvasOutOfBounds() {
        final SoundContent sound = new SoundContent(SOUND_1_ID).setDuration(CANVAS_DURATION + 1);
        final MediaFragmentSelector selector = new MediaFragmentSelector(0, 0, WIDTH, HEIGHT);

        myCanvas.setWidthHeight(WIDTH, HEIGHT).setDuration(CANVAS_DURATION).paintWith(selector, sound);
    }

    /**
     * Tests painting a sound outside the bounds of a temporal fragment of a canvas with spatiotemporal dimensions.
     */
    @Test(expected = ContentOutOfBoundsException.class)
    public final void testPaintSoundOnTemporalFragmentOfSpatiotemporalCanvasOutOfBounds() {
        final SoundContent sound = new SoundContent(SOUND_1_ID).setDuration(DURATION);
        final MediaFragmentSelector selector = new MediaFragmentSelector(0.0f, DURATION - 1);

        myCanvas.setWidthHeight(WIDTH, HEIGHT).setDuration(CANVAS_DURATION).paintWith(selector, sound);
    }

    /**
     * Tests painting a sound outside the bounds of a spatiotemporal fragment of a canvas with spatiotemporal
     * dimensions.
     */
    @Test(expected = ContentOutOfBoundsException.class)
    public final void testPaintSoundOnSpatiotemporalFragmentOfSpatiotemporalCanvasOutOfBounds() {
        final SoundContent sound = new SoundContent(SOUND_1_ID).setDuration(DURATION);
        final MediaFragmentSelector selector = new MediaFragmentSelector(0, 0, WIDTH, HEIGHT, 0.0f, DURATION - 1);

        myCanvas.setWidthHeight(WIDTH, HEIGHT).setDuration(CANVAS_DURATION).paintWith(selector, sound);
    }

    /**
     * Tests painting a video outside the bounds of a spatial fragment of a canvas with spatiotemporal dimensions.
     */
    @Test(expected = ContentOutOfBoundsException.class)
    public final void testPaintVideoOnSpatialFragmentOfSpatiotemporalCanvasOutOfBounds() {
        final VideoContent video = new VideoContent(VIDEO_1_ID).setWidthHeight(WIDTH, HEIGHT).setDuration(DURATION);
        final MediaFragmentSelector selector = new MediaFragmentSelector(0, 0, WIDTH - 1, HEIGHT);

        myCanvas.setWidthHeight(WIDTH, HEIGHT).setDuration(CANVAS_DURATION).paintWith(selector, video);
    }

    /**
     * Tests painting a video outside the bounds of a temporal fragment of a canvas with spatiotemporal dimensions.
     */
    @Test(expected = ContentOutOfBoundsException.class)
    public final void testPaintVideoOnTemporalFragmentOfSpatiotemporalCanvasOutOfBounds() {
        final VideoContent video = new VideoContent(VIDEO_1_ID).setWidthHeight(WIDTH, HEIGHT).setDuration(DURATION);
        final MediaFragmentSelector selector = new MediaFragmentSelector(0.0f, DURATION - 1);

        myCanvas.setWidthHeight(WIDTH, HEIGHT).setDuration(CANVAS_DURATION).paintWith(selector, video);
    }

    /**
     * Tests painting a video outside the bounds of a spatiotemporal fragment of a canvas with spatiotemporal
     * dimensions.
     */
    @Test(expected = ContentOutOfBoundsException.class)
    public final void testPaintVideoOnSpatiotemporalFragmentOfSpatiotemporalCanvasOutOfBounds() {
        final VideoContent video = new VideoContent(VIDEO_1_ID).setWidthHeight(WIDTH + 1, HEIGHT).setDuration(DURATION);
        final MediaFragmentSelector selector = new MediaFragmentSelector(0, 0, WIDTH, HEIGHT, 0.0f, DURATION);

        myCanvas.setWidthHeight(WIDTH, HEIGHT).setDuration(CANVAS_DURATION).paintWith(selector, video);
    }

    /**********************************
     * Invalid media fragment strings *
     **********************************/

    /**
     * Tests painting an image onto a canvas fragment specified with an invalid URI media fragment component.
     */
    @Test(expected = IllegalArgumentException.class)
    public final void testPaintImageInvalidFragment() {
        final ImageContent image = new ImageContent(IMAGE_1_ID);

        myCanvas.setWidthHeight(WIDTH, HEIGHT).paintWith("xywh=", image);
    }

    /**
     * Tests painting a sound onto a canvas fragment specified with an invalid URI media fragment component.
     */
    @Test(expected = IllegalArgumentException.class)
    public final void testPaintSoundInvalidFragment() {
        final SoundContent sound = new SoundContent(SOUND_1_ID);

        myCanvas.setDuration(CANVAS_DURATION).paintWith("t=", sound);
    }

    /**
     * Tests painting a video onto a canvas fragment specified with an invalid URI media fragment component.
     */
    @Test(expected = IllegalArgumentException.class)
    public final void testPaintVideoInvalidFragment() {
        final VideoContent video = new VideoContent(VIDEO_1_ID);

        myCanvas.setWidthHeight(WIDTH, HEIGHT).setDuration(CANVAS_DURATION).paintWith("xywh=&t=", video);
    }

    /*****************
     * Serialization *
     *****************/

    /**
     * Tests serializing and deserializing a canvas.
     *
     * @throws IOException If there is trouble reading the canvas file or serializing the constructed canvas
     */
    @Test
    public final void testSerialization() throws IOException {
        final JsonObject expected;
        final JsonObject found;

        final ImageContent imageContent = new ImageContent(IMAGE_1_ID).setWidthHeight(WIDTH, HEIGHT);
        final PaintingAnnotation paintingAnno = new PaintingAnnotation(IMAGE_ANNO_ID, myCanvas)
                .setBody(imageContent).setTarget(myCanvas.getID());
        final TextContent textContent = new TextContent(TEXT_ID);
        final SupplementingAnnotation supplementingAnno = new SupplementingAnnotation(TEXT_ANNO_ID, myCanvas)
                .setBody(textContent).setTarget(myCanvas.getID());

        myCanvas.setWidthHeight(WIDTH, HEIGHT);
        myCanvas.setThumbnails(new ImageContent(IMAGE_THUMBNAIL_ID).setWidthHeight(THUMBNAIL_WH, THUMBNAIL_WH)
                .setService(new ImageInfoService(IMAGE_THUMBNAIL_SERVICE_ID)));
        myCanvas.setPaintingPages(
                new AnnotationPage<PaintingAnnotation>(IMAGE_PAGE_ID).addAnnotations(paintingAnno));
        myCanvas.setSupplementingPages(
                new AnnotationPage<SupplementingAnnotation>(TEXT_PAGE_ID).addAnnotations(supplementingAnno));

        expected = new JsonObject(StringUtils.read(CANVAS_FULL));
        found = new JsonObject(TestUtils.toJson(myCanvas));

        assertEquals(expected, found);
    }

    /**
     * Tests serializing and deserializing a canvas constructed using Canvas.paintWith() and Canvas.supplementWith().
     *
     * @throws IOException If there is trouble reading the canvas file or serializing the constructed canvas
     */
    @Test
    public final void testSerialization2() throws IOException {
        final JsonObject expected;
        final JsonObject found;

        final ImageContent image = new ImageContent(IMAGE_1_ID).setWidthHeight(WIDTH, HEIGHT);
        final TextContent text = new TextContent(TEXT_ID);
        final Thumbnail thumbnail = new ImageContent(IMAGE_THUMBNAIL_ID).setWidthHeight(THUMBNAIL_WH, THUMBNAIL_WH)
                .setService(new ImageInfoService(IMAGE_THUMBNAIL_SERVICE_ID));

        myCanvas.setWidthHeight(WIDTH, HEIGHT).setThumbnails(thumbnail).paintWith(image).supplementWith(text);

        expected = new JsonObject(StringUtils.read(CANVAS_FULL));
        found = new JsonObject(TestUtils.toJson(myCanvas));

        assertEquals(expected, found);
    }

    /**
     * Tests serializing and deserializing a spatial canvas painted with an image.
     *
     * @throws IOException If there is trouble reading the canvas file or serializing the constructed canvas
     */
    @Test
    public final void testPaintImageSerialization() throws IOException {
        final JsonObject expected;
        final JsonObject found;

        final ImageContent image = new ImageContent(IMAGE_1_ID).setWidthHeight(WIDTH, HEIGHT);

        myCanvas.setWidthHeight(WIDTH, HEIGHT).paintWith(image);

        expected = new JsonObject(StringUtils.read(CANVAS_IMAGE));
        found = new JsonObject(TestUtils.toJson(myCanvas));

        assertEquals(expected, found);
    }

    /**
     * Tests serializing and deserializing a canvas painted with two images, each intended as a choice between
     * alternate representations.
     *
     * @throws IOException If there is trouble reading the canvas file or serializing the constructed canvas
     */
    @Test
    public final void testPaintImageChoiceSerialization() throws IOException {
        final JsonObject expected;
        final JsonObject found;

        final ImageContent image1 = new ImageContent(IMAGE_1_ID).setWidthHeight(WIDTH, HEIGHT);
        final ImageContent image2 = new ImageContent(IMAGE_2_ID).setWidthHeight(WIDTH, HEIGHT);

        myCanvas.setWidthHeight(WIDTH, HEIGHT).paintWith(image1, image2);

        expected = new JsonObject(StringUtils.read(CANVAS_IMAGE_CHOICE));
        found = new JsonObject(TestUtils.toJson(myCanvas));

        assertEquals(expected, found);
    }

    /**
     * Tests serializing and deserializing a canvas painted with two images, each on different fragments of the canvas
     * specified by a URI media fragment component.
     *
     * @throws IOException If there is trouble reading the canvas file or serializing the constructed canvas
     */
    @Test
    public final void testPaintImageMultiSerialization() throws IOException {
        final JsonObject expected;
        final JsonObject found;

        final ImageContent image1 = new ImageContent(IMAGE_1_ID).setWidthHeight(WIDTH, HEIGHT);
        final ImageContent image2 = new ImageContent(IMAGE_2_ID).setWidthHeight(WIDTH, HEIGHT);

        final String selector1 = StringUtils.format(URI_FRAGMENT_XYWH_TEMPLATE, 0, 0, WIDTH, HEIGHT);
        final String selector2 = StringUtils.format(URI_FRAGMENT_XYWH_TEMPLATE, 0, HEIGHT, WIDTH, HEIGHT);

        myCanvas.setWidthHeight(WIDTH, HEIGHT * 2).paintWith(selector1, image1).paintWith(selector2, image2);

        expected = new JsonObject(StringUtils.read(CANVAS_IMAGE_MULTI));
        found = new JsonObject(TestUtils.toJson(myCanvas));

        assertEquals(expected, found);
    }

    /**
     * Tests serializing and deserializing a canvas painted with two images, each on different fragments of the canvas
     * specified by a {@link MediaFragmentSelector}.
     *
     * @throws IOException If there is trouble reading the canvas file or serializing the constructed canvas
     */
    @Test
    public final void testPaintImageMultiFragmentSelectorSerialization() throws IOException {
        final JsonObject expected;
        final JsonObject found;

        final ImageContent image1 = new ImageContent(IMAGE_1_ID).setWidthHeight(WIDTH, HEIGHT);
        final ImageContent image2 = new ImageContent(IMAGE_2_ID).setWidthHeight(WIDTH, HEIGHT);

        final MediaFragmentSelector selector1 = new MediaFragmentSelector(StringUtils.format(URI_FRAGMENT_XYWH_TEMPLATE,
                0, 0, WIDTH, HEIGHT));
        final MediaFragmentSelector selector2 = new MediaFragmentSelector(StringUtils.format(URI_FRAGMENT_XYWH_TEMPLATE,
                0, HEIGHT, WIDTH, HEIGHT));

        myCanvas.setWidthHeight(WIDTH, HEIGHT * 2).paintWith(selector1, image1).paintWith(selector2, image2);

        expected = new JsonObject(StringUtils.read(CANVAS_IMAGE_MULTI));
        found = new JsonObject(TestUtils.toJson(myCanvas));

        assertEquals(expected, found);
    }

    /**
     * Tests serializing and deserializing a canvas painted with a sound.
     *
     * @throws IOException If there is trouble reading the canvas file or serializing the constructed canvas
     */
    @Test
    public final void testPaintSoundSerialization() throws IOException {
        final JsonObject expected;
        final JsonObject found;

        final SoundContent sound = new SoundContent(SOUND_1_ID).setDuration(DURATION);

        myCanvas = new Canvas(SOUND_CANVAS_ID, LABEL).setDuration(CANVAS_DURATION).paintWith(sound);

        expected = new JsonObject(StringUtils.read(CANVAS_SOUND));
        found = new JsonObject(TestUtils.toJson(myCanvas));

        assertEquals(expected, found);
    }

    /**
     * Tests serializing and deserializing a canvas painted with sound twice: first with two sounds each intended as a
     * choice between alternate representations, and then with another sound on a different fragment of the canvas
     * specified by a URI media fragment component.
     *
     * @throws IOException If there is trouble reading the canvas file or serializing the constructed canvas
     */
    @Test
    public final void testPaintSoundChoiceMultiSerialization() throws IOException {
        final JsonObject expected;
        final JsonObject found;

        final SoundContent sound1 = new SoundContent(SOUND_1_ID).setDuration(DURATION);
        final SoundContent sound2 = new SoundContent(SOUND_2_ID).setDuration(DURATION);
        final SoundContent sound3 = new SoundContent(SOUND_3_ID).setDuration(DURATION);

        final String selector1 = StringUtils.format(URI_FRAGMENT_T_TEMPLATE, 0, DURATION);
        final String selector2 = StringUtils.format(URI_FRAGMENT_T_TEMPLATE, DURATION, DURATION + DURATION);

        myCanvas = new Canvas(SOUND_CANVAS_ID, LABEL).setDuration(CANVAS_DURATION)
                .paintWith(selector1, sound1, sound2).paintWith(selector2, sound3);

        expected = new JsonObject(StringUtils.read(CANVAS_SOUND_CHOICE_MULTI));
        found = new JsonObject(TestUtils.toJson(myCanvas));

        assertEquals(expected, found);
    }

    /**
     * Tests serializing and deserializing a canvas painted with sound twice: first with two sounds each intended as a
     * choice between alternate representations, and then with another sound on a different fragment of the canvas
     * specified by a {@link MediaFragmentSelector}.
     *
     * @throws IOException If there is trouble reading the canvas file or serializing the constructed canvas
     */
    @Test
    public final void testPaintSoundChoiceMultiFragmentSelectorSerialization() throws IOException {
        final JsonObject expected;
        final JsonObject found;

        final SoundContent sound1 = new SoundContent(SOUND_1_ID).setDuration(DURATION);
        final SoundContent sound2 = new SoundContent(SOUND_2_ID).setDuration(DURATION);
        final SoundContent sound3 = new SoundContent(SOUND_3_ID).setDuration(DURATION);

        final MediaFragmentSelector selector1 = new MediaFragmentSelector(
                StringUtils.format(URI_FRAGMENT_T_TEMPLATE, 0, DURATION));
        final MediaFragmentSelector selector2 = new MediaFragmentSelector(
                StringUtils.format(URI_FRAGMENT_T_TEMPLATE, DURATION, DURATION + DURATION));

        myCanvas = new Canvas(SOUND_CANVAS_ID, LABEL).setDuration(CANVAS_DURATION)
                .paintWith(selector1, sound1, sound2).paintWith(selector2, sound3);

        expected = new JsonObject(StringUtils.read(CANVAS_SOUND_CHOICE_MULTI));
        found = new JsonObject(TestUtils.toJson(myCanvas));

        assertEquals(expected, found);
    }

    /**
     * Tests serializing and deserializing a canvas painted with a video.
     *
     * @throws IOException If there is trouble reading the canvas file or serializing the constructed canvas
     */
    @Test
    public final void testPaintVideoSerialization() throws IOException {
        final JsonObject expected;
        final JsonObject found;

        final VideoContent video = new VideoContent(VIDEO_1_ID).setWidthHeight(WIDTH, HEIGHT).setDuration(DURATION);

        myCanvas = new Canvas(VIDEO_CANVAS_ID, LABEL).setWidthHeight(WIDTH, HEIGHT).setDuration(CANVAS_DURATION)
                .paintWith(video);

        expected = new JsonObject(StringUtils.read(CANVAS_VIDEO));
        found = new JsonObject(TestUtils.toJson(myCanvas));

        assertEquals(expected, found);
    }

    /**
     * Tests serializing and deserializing a canvas painted with two videos, each on different fragments of the canvas
     * specified by a URI media fragment component.
     *
     * @throws IOException If there is trouble reading the canvas file or serializing the constructed canvas
     */
    @Test
    public final void testPaintVideoMultiSerialization() throws IOException {
        final JsonObject expected;
        final JsonObject found;

        final VideoContent video1 = new VideoContent(VIDEO_1_ID).setWidthHeight(WIDTH, HEIGHT).setDuration(DURATION);
        final VideoContent video2 = new VideoContent(VIDEO_2_ID).setWidthHeight(WIDTH, HEIGHT).setDuration(DURATION);

        final String selector1 = StringUtils.format(URI_FRAGMENT_XYWHT_TEMPLATE, 0, 0, WIDTH, HEIGHT, 0, DURATION);
        final String selector2 = StringUtils.format(URI_FRAGMENT_XYWHT_TEMPLATE, 0, 0, WIDTH, HEIGHT, DURATION,
                DURATION + DURATION);

        myCanvas = new Canvas(VIDEO_CANVAS_ID, LABEL).setWidthHeight(WIDTH, HEIGHT).setDuration(CANVAS_DURATION)
                .paintWith(selector1, video1).paintWith(selector2, video2);

        expected = new JsonObject(StringUtils.read(CANVAS_VIDEO_MULTI));
        found = new JsonObject(TestUtils.toJson(myCanvas));

        assertEquals(expected, found);
    }

    /**
     * Tests serializing and deserializing a canvas painted with two videos, each on different fragments of the canvas
     * specified by a {@link MediaFragmentSelector}.
     *
     * @throws IOException If there is trouble reading the canvas file or serializing the constructed canvas
     */
    @Test
    public final void testPaintVideoMultiFragmentSelectorSerialization() throws IOException {
        final JsonObject expected;
        final JsonObject found;

        final VideoContent video1 = new VideoContent(VIDEO_1_ID).setWidthHeight(WIDTH, HEIGHT).setDuration(DURATION);
        final VideoContent video2 = new VideoContent(VIDEO_2_ID).setWidthHeight(WIDTH, HEIGHT).setDuration(DURATION);

        final MediaFragmentSelector selector1 = new MediaFragmentSelector(
                StringUtils.format(URI_FRAGMENT_XYWHT_TEMPLATE, 0, 0, WIDTH, HEIGHT, 0, DURATION));
        final MediaFragmentSelector selector2 = new MediaFragmentSelector(
                StringUtils.format(URI_FRAGMENT_XYWHT_TEMPLATE, 0, 0, WIDTH, HEIGHT, DURATION, DURATION + DURATION));

        myCanvas = new Canvas(VIDEO_CANVAS_ID, LABEL).setWidthHeight(WIDTH, HEIGHT).setDuration(CANVAS_DURATION)
                .paintWith(selector1, video1).paintWith(selector2, video2);

        expected = new JsonObject(StringUtils.read(CANVAS_VIDEO_MULTI));
        found = new JsonObject(TestUtils.toJson(myCanvas));

        assertEquals(expected, found);
    }

    /**
     * Returns the ID of the content resource painted on myCanvas.
     */
    private URI getContentResourceID() {
        return myCanvas.getPaintingPages().get(0).getAnnotations().get(0).getBody().get(0).getID();
    }

    /**
     * Returns the value of the media fragment of the selector that targets myCanvas.
     */
    private String getMediaFragment() {
        return ((MediaFragmentSelector) ((SpecificResource) myCanvas.getPaintingPages().get(0).getAnnotations().get(0)
                .getTarget()).getSelector()).getValue();
    }

}
