package ru.volkov.batch.sotialuko.exporter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.volkov.batch.AppProperties;
import ru.volkov.batch.sotialuko.service.ServiceDb;
import ru.volkov.batch.sotialuko.dto.StPrigorodRjdCard;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс экспортирующий данные активных карт с правом на льготный проезд. Экспорт происходит в локальный файл.
 * Файл хранится в папке /temp
 */
@Component
@Slf4j
public class SotialUkoExporter {

    private ServiceDb service;
    private AppProperties properties;

    @Autowired
    public SotialUkoExporter(ServiceDb service, AppProperties properties){
        this.service = service;
        this.properties = properties;
    }

    /**
     * Экспортирует сведения об активных картах с правом на льготный проезд в файл в каталог /temp
     */
    public void exportToFile() {
        List<String> data = makeData();
        String fileName = makeFileName();
        Path pathToFile = makePath(fileName);
        makeLocalFile(pathToFile);
        writeToFile(pathToFile, data);
    }

    /**
     * Возвращает список активных карт с правом на льготный проезд
     * @return список карт
     */
    public List<String> makeData(){
        List<StPrigorodRjdCard> cardList = service.getCardsWithActiveRjd(LocalDateTime.now());
        List<String> data = new ArrayList<>();
        cardList.stream()
                .map(StPrigorodRjdCard::getCard_bsk_num)
                .forEach(data::add);
        return data;
    }

    /**
     * Возвращает имя файла для записи в формате:"SOTIALUKO_YYYY-MM-DD.csv". Формат имени файла состоит из трех частей:
     * <ul>
     *     <li><b>SOTIALUKO_</b> – префикс файла, определяющий, что файл содержит Реестр УКО с правом на льготный проезд на РЖД</li>
     *     <li><b>YYYY-MM-DD</b> – дата формирования файла, с 4 (четырьмя) знаками года</li>
     *     <li><b>.csv</b> - расширение файла</li>
     * </ul>
     * Конкретные значения частей формата берутся из application.properties
     * @return  имя файла
     */
    public String makeFileName(){
        String prefix = properties.getSotialUkoFilePrefix();
        String suffix = properties.getSotialUkoFileSuffix();
        return prefix + LocalDate.now().toString() + suffix;
    }

    /**
     * Создает файл по заданному пути
     * @param path путь
     */
    public void makeLocalFile(Path path) {
        try {
            Files.createFile(path);
            log.info("File with path {} create successfully", path.toString());
        } catch (FileAlreadyExistsException e) {
            log.debug("File with path {} already exists", path.toString());
            e.printStackTrace();
        } catch (IOException e) {
            log.debug("File with path {} wasn't created", path.toString());
            e.printStackTrace();
        }

    }

    /**
     * Производит запись данных в файл
     * @param path путь к файлу
     * @param dataToWrite данные для записи
     */
    public void writeToFile(Path path, List<String> dataToWrite) {
        try {
            Files.write(path, dataToWrite);
            log.info("Data writes to file {} successfully", path.toString());
        } catch (IOException e) {
            log.debug("Data writes to file {} failed", path.toString());
            e.printStackTrace();
        }
    }

    /**
     * Создает путь к файлу.
     * @param fileName - имя файла
     * @return путь
     */
    public Path makePath(String fileName) {
        Path path = Paths.get(properties.getTempDir(), fileName);
        log.info("Path: {}", path.toString());
        return path;
    }
}
