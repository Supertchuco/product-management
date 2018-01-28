package com.products.productmanagement.unit;

import com.products.productmanagement.entity.Image;
import com.products.productmanagement.exception.DuplicatedImageTypeOnDatabaseException;
import com.products.productmanagement.exception.ImageNotFoundException;
import com.products.productmanagement.exception.ImageTypeAlreadyExistException;
import com.products.productmanagement.repository.ImageRepository;
import com.products.productmanagement.service.ImageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ImageServiceTest {

    @InjectMocks
    ImageService imageService;

    @Mock
    ImageRepository imageRepository;

    @Test
    public void updateImageByTypeHappyScenarioTest() {
        Image imageTest = new Image("photo");
        Image imageFoundDB = new Image("jpeg");
        Mockito.when(imageRepository.findByImageType("jpeg")).thenReturn(new ArrayList<Image>() {{
            add(imageFoundDB);
        }});
        Mockito.when(imageRepository.save(imageTest)).thenReturn(imageTest);
        Image imageReturn = imageService.updateImageByType("jpeg", imageTest);
        assertEquals(imageReturn.getImageType(), imageTest.getImageType());
        assertEquals(imageReturn.getProduct(), imageTest.getProduct());
    }

    @Test(expected = ImageNotFoundException.class)
    public void updateImageByTypeWhenImageNotFoundTest() {
        Image imageTest = new Image("photo");
        imageService.updateImageByType("pixel", imageTest);
    }

    @Test(expected = DuplicatedImageTypeOnDatabaseException.class)
    public void updateImageTypeWhenImageExistOnDatabaseTest() {
        Image imageTest = new Image("photo");
        Mockito.when(imageRepository.findByImageType("photo")).thenReturn(new ArrayList<Image>() {{
            add(imageTest);
            add(imageTest);
        }});
        imageService.updateImageByType("photo", imageTest);
    }

    @Test
    public void saveNewImageHappyScenarioTest() {
        Image imageTest = new Image("photo");
        Mockito.when(imageRepository.save(imageTest)).thenReturn(imageTest);
        Image imageReturn = imageService.saveNewImage(imageTest);
        assertEquals(imageReturn.getImageType(), imageTest.getImageType());
        assertEquals(imageReturn.getProduct(), imageTest.getProduct());
    }

    @Test(expected = ImageTypeAlreadyExistException.class)
    public void saveNewImageTestWhenImageExistOnDatabaseTest() {
        Image imageTest = new Image("photo");
        Mockito.when(imageRepository.findByImageType("photo")).thenReturn(new ArrayList<Image>() {{
            add(imageTest);
            add(imageTest);
        }});
        imageService.saveNewImage(imageTest);
    }

    @Test
    public void findImageByTypeTest() {
        Image imageTest = new Image("photo");
        Mockito.when(imageRepository.findByImageType("photo")).thenReturn(new ArrayList<Image>() {{
            add(imageTest);
        }});
        Image image = imageService.findImageByType("photo");
        assertEquals(image.getImageType(), imageTest.getImageType());
        assertEquals(image.getProduct(), imageTest.getProduct());
    }

    @Test
    public void findImageByTypeWhenNotFoundOnDatabaseTest() {
        Image image = imageService.findImageByType("photo");
        assertEquals(null, image);
    }

    @Test(expected = DuplicatedImageTypeOnDatabaseException.class)
    public void findImageByTypeWhenImageIsDuplicatedOnDatabaseTest() {
        Image imageTest = new Image("photo");
        Mockito.when(imageRepository.findByImageType("photo")).thenReturn(new ArrayList<Image>() {{
            add(imageTest);
            add(imageTest);
        }});
        imageService.findImageByType("photo");
    }

    @Test
    public void deleteImageTest() {
        Image imageTest = new Image("photo");
        Mockito.when(imageRepository.findByImageType("photo")).thenReturn(new ArrayList<Image>() {{
            add(imageTest);
        }});
        try {
            imageService.deleteImageByType("photo");
        } catch (Exception ex) {
            assertFalse(true);
        }
    }

    @Test(expected = ImageNotFoundException.class)
    public void deleteImageWhenImageNotExistOnDatabaseTest() {
        imageService.deleteImageByType("photo");
    }

    @Test(expected = DuplicatedImageTypeOnDatabaseException.class)
    public void deleteImageWhenImageIsDuplicatedOnDatabaseTest() {
        Image imageTest = new Image("photo");
        Mockito.when(imageRepository.findByImageType("photo")).thenReturn(new ArrayList<Image>() {{
            add(imageTest);
            add(imageTest);
        }});
        imageService.deleteImageByType("photo");
    }

    @Test
    public void buildImagesCollectionsToInsertProductOnDatabaseTest() {
        Image imageTest1 = new Image("photo");
        Image imageTest2 = new Image("png");
        Mockito.when(imageRepository.findByImageType("photo")).thenReturn(new ArrayList<Image>() {{
            add(imageTest1);
        }});
        Mockito.when(imageRepository.findByImageType("png")).thenReturn(new ArrayList<Image>() {{
            add(imageTest2);
        }});
        Collection<Image> imagesReturned = imageService.buildImagesCollectionsToInsertProductOnDatabase(new ArrayList<Image>() {{
            add(imageTest1);
            add(imageTest2);
        }});
        assertEquals(imagesReturned.size(), 2);
    }

    @Test
    public void buildImagesCollectionsToInsertProductOnDatabaseWhenInputIsEmptyTest() {
        Collection<Image> imagesReturned = imageService.buildImagesCollectionsToInsertProductOnDatabase(new ArrayList<Image>());
        assertEquals(0, imagesReturned.size());
    }

    @Test
    public void buildImagesCollectionsToInsertProductOnDatabaseWhenInputIsNullTest() {
        Collection<Image> imagesReturned = imageService.buildImagesCollectionsToInsertProductOnDatabase(null);
        assertEquals(0, imagesReturned.size());
    }

    @Test
    public void validateImagesFoundOnDatabaseTest() {
        Image imageTest1 = new Image("photo");
        Image imageTest2 = new Image("png");
        Mockito.when(imageRepository.findByImageType("photo")).thenReturn(new ArrayList<Image>() {{
            add(imageTest1);
        }});
        Mockito.when(imageRepository.findByImageType("png")).thenReturn(new ArrayList<Image>() {{
            add(imageTest2);
        }});
        Collection<Image> imagesReturned = imageService.buildImagesCollectionsToInsertProductOnDatabase(new ArrayList<Image>() {{
            add(imageTest1);
            add(imageTest2);
        }});
        assertEquals(imagesReturned.size(), 2);
    }

}
