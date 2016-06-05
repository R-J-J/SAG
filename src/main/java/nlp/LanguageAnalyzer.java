package nlp;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import javafx.util.Pair;
import org.getopt.stempel.Stemmer;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import pl.sgjp.morfeusz.Morfeusz;
import pl.sgjp.morfeusz.MorphInterpretation;
import pl.sgjp.morfeusz.ResultsIterator;
import pl.sgjp.morfeusz.app.MorfeuszUtils;
import statistics.Statistics;

import java.util.*;

/**
 * Created by Arjan on 05.06.2016.
 */
public class LanguageAnalyzer {

    private static final Set<String> stopWords = Sets.newHashSet();
    private static final Set<String> isPhrases = Sets.newHashSet("jest", "są", "to", "był", "była", "było", "byli", "były", "będzie", "będą", "zostanie", "zostaną");

    private final Map<String, MorphInterpretation> phraseInterpretations;
    private final Stemmer stemmer = new Stemmer();
    private final Morfeusz morfeusz = Morfeusz.createInstance();

    private final Set<ObjectProperty> objectProperties = new HashSet<>();

    public LanguageAnalyzer(String[] phraseArray) {
        phraseInterpretations = new HashMap<>(phraseArray.length);
        for(String phrase: phraseArray) {
            phraseInterpretations.put(phrase, LanguageUtils.findBasicNoun(phrase, morfeusz));
        }
    }

    public void analyzeTable(Element table, String object) {
      /*  Elements trs = table.getElementsByTag("tr");
        if(trs.size() < 2) {
            return;
        }
        if(!isPropertyTable(trs)) {
            return;
        }
        for(Element tr: trs) {
            Elements tds = tr.getElementsByTag("td");
            String property = tds.get(0).text();
            String value = tds.get(1).text();
            if (!Strings.isNullOrEmpty(property) && !Strings.isNullOrEmpty(value)) {
                objectProperties.add(new ObjectProperty(object, property, value));
            }
        }*/
    }

    private boolean isPropertyTable(Elements trs) {
        for(Element tr: trs) {
            Elements tds = tr.getElementsByTag("td");
            if(tds.size() == 2) {
                return true;
            }
        }
        return false;
    }

    public void analyzeText(String text) {

        if(Strings.isNullOrEmpty(text)) {
            return;
        }

        ResultsIterator results = morfeusz.analyseAsIterator(text);

        while(results.hasNext()) {
            MorphInterpretation interpretation = results.next();
            System.out.println(MorfeuszUtils.getInterpretationString(interpretation, morfeusz));
        }

        String[] words = text.toLowerCase().replaceAll("\\.\\?,!", "").split(" ");
        for(int i=0; i<words.length; ++i) {
            String word = words[i];
            if(Strings.isNullOrEmpty(word)) {
                continue;
            }
            for (Map.Entry<String, MorphInterpretation> entry: phraseInterpretations.entrySet()) {
                if (word.equals(entry.getKey()) || stemmer.stem(word, true).equals(entry.getValue())) {
                    findIsStatement(words, i);
                }
            }
        }
    }

    public LanguageAnalysis getResult() {
        return new LanguageAnalysis(objectProperties);
    }

    private void findIsStatement(String[] words, int position) {
        if(position+2 < words.length && isPhrases.contains(words[position+1]) && !stopWords.contains(words[position+2])) {
            objectProperties.add(new ObjectProperty(words[position], words[position+2], "true"));
            Statistics.stat(Statistics.StatisticsEvent.IS_STATEMENTS_FOUND);
        }
    }

}
