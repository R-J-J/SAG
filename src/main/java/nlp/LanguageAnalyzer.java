package nlp;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import pl.sgjp.morfeusz.Morfeusz;
import pl.sgjp.morfeusz.MorphInterpretation;
import pl.sgjp.morfeusz.app.MorfeuszUtils;

import java.util.*;

/**
 * Created by Arjan on 05.06.2016.
 */
public class LanguageAnalyzer implements AnalysisBuilder {

    private static final Set<String> stopWords = Sets.newHashSet();

    private final Map<String, MorphInterpretation> phraseInterpretations;
    private final Morfeusz morfeusz = Morfeusz.createInstance();

    private final Set<ObjectProperty> objectProperties = new HashSet<>();
    private final Set<ObjectSubclass> objectSubclasses = new HashSet<>();

    private final List<Rule> rules = new ArrayList<>();

    public LanguageAnalyzer(String[] phraseArray) {
        phraseInterpretations = new HashMap<>(phraseArray.length);
        for(String phrase: phraseArray) {
            MorphInterpretation noun = LanguageUtils.findNoun(phrase, morfeusz);
            if(noun != null) {
                System.out.println("Adding lemma "+noun.getLemma()+" for word "+noun.getOrth());
                phraseInterpretations.put(noun.getLemma(), noun);
            } else {
                System.out.println(phrase+" is not a valid noun");
            }
        }

        rules.add(new IsRule(this, morfeusz));
        rules.add(new PropertyRule(this, morfeusz));
    }

    @Override
    public void addObjectProperty(ObjectProperty objectProperty) {
        objectProperties.add(objectProperty);
    }

    @Override
    public void addObjectSubclass(ObjectSubclass objectSubclass) {
        objectSubclasses.add(objectSubclass);
    }

    public Set<ObjectProperty> getObjectProperties() {
        return objectProperties;
    }

    public Set<ObjectSubclass> getObjectSubclasses() {
        return objectSubclasses;
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

        List<MorphInterpretation> results = morfeusz.analyseAsList(text);

        for(int i=0; i<results.size(); ++i) {
           // System.out.println(MorfeuszUtils.getInterpretationString(results.get(i), morfeusz));
            if(phraseInterpretations.containsKey(results.get(i).getLemma())) {
                for(Rule rule: rules) {
                    rule.checkRule(results, i);
                }
            }
        }

/*
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
        }*/
    }

/*
    private void findIsStatement(String[] words, int position) {
        if(position+2 < words.length && isPhrases.contains(words[position+1]) && !stopWords.contains(words[position+2])) {
            objectProperties.add(new ObjectProperty(words[position], words[position+2], "true"));
            Statistics.stat(Statistics.StatisticsEvent.IS_STATEMENTS_FOUND);
        }
    }
*/
}
