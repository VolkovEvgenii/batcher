package ru.volkov.batch.sotialuko.exporter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.volkov.batch.util.db.ServiceDb;
import ru.volkov.batch.util.db.StPrigorodRjdCard;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    @Autowired
    public SotialUkoExporter(ServiceDb service){
        this.service = service;
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
     * Возвращает имя файла для записи
     * @return  имя файла
     */
    public String makeFileName(){
        return "SOTIALUKO_" + LocalDateTime.now() + ".csv";
    }

    /**
     * Создает файл по пути
     * @param path путь
     */
    public void makeLocalFile(Path path) {
        try {
            Files.createFile(path);
            log.info("File with path {} create successfully" + path.toString());
        } catch (IOException e) {
            log.debug("File with path {} wasn't created" + path.toString());
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
            log.info("Data writes to file {} successfully" + path.toString());
        } catch (IOException e) {
            log.debug("Data writes to file {} failed" + path.toString());
            e.printStackTrace();
        }
    }

    /**
     * Создает путь к файлу
     * @param fileName - имя файла
     * @return путь
     */
    public Path makePath(String fileName) {
        Path path = Paths.get("temp", fileName);
        log.info("Path: {}", path.toString());
        return path;
    }
}
