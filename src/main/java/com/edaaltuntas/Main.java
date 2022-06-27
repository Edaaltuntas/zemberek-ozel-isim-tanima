package com.edaaltuntas;

import zemberek.morphology.TurkishMorphology;
import zemberek.ner.NamedEntity;
import zemberek.ner.NerSentence;
import zemberek.ner.PerceptronNer;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static Path modelPath = Path.of(ClassLoader.getSystemResource("model").getPath());
    public static void main(String[] args) {
        Logger.getLogger("").setLevel(Level.OFF);
        if ( args.length > 0 ){
            modelPath = Path.of(args[0]).toAbsolutePath();
        }
        while(true) {
            try {
                Scanner s = new Scanner(System.in);
                System.out.println("Merhabalar, Size Yardımcı Olabilmemiz İçin Sorundan Kısaca Bahsedebilir misiniz?");
                String line = s.nextLine();
                if (Objects.equals(line, "") || Objects.equals(line, "q")) {
                    break;
                }else {
                    NerSentence result = getNames(line);
                    printNames(result);
                }
            }catch (Exception e) {
                break;
            }
        }
    }

    public static void printNames(NerSentence result) {
        System.out.printf("Verilen metinde %d kelime bulunuyor.%n", result.getAllEntities().size());
        List<NamedEntity> entities = result.getNamedEntities();
        int i = 0;
        for (NamedEntity entity : entities) {
            System.out.printf("ÖZEL İSİM: %s %n", entity);
            i++;
        }
        System.out.printf("%d tane özel isim bulundu.%n", i);
    }

    public static NerSentence getNames(String sentence) throws IOException {
        TurkishMorphology morphology = TurkishMorphology.createWithDefaults();
        PerceptronNer ner = PerceptronNer.loadModel(modelPath, morphology);
        return ner.findNamedEntities(sentence);
    }
}
