package nlp;

import pl.sgjp.morfeusz.Morfeusz;
import pl.sgjp.morfeusz.MorphInterpretation;

import java.util.List;

/**
 * Created by Arjan on 05.06.2016.
 */
public abstract class LanguageUtils {

    public static final String NOUN = "subst";
    public static final String ADJECTIVE = "adj";

    private LanguageUtils() {

    }

    public static MorphInterpretation findNoun(String word, Morfeusz morfeusz) {
        return findByType(word, morfeusz, NOUN);
    }

    public static MorphInterpretation findByType(String word, Morfeusz morfeusz, String... types) {
        return findByType(morfeusz.analyseAsList(word), morfeusz, types);
    }

    public static MorphInterpretation findByType(List<MorphInterpretation> morphInterpretationList, Morfeusz morfeusz, String... types) {
        for(MorphInterpretation interpretation: morphInterpretationList) {
            String foundType = morfeusz.getIdResolver().getTag(interpretation.getTagId());
            if(isType(foundType.split(":")[0], types)) {
                return interpretation;
            }
        }
        return null;
    }

    private static boolean isType(String s, String[] types) {
        for(String type: types) {
            if(s.equals(type)) {
                return true;
            }
        }
        return false;
    }

    public static MorphInterpretation findByLemma(String word, String lemma, Morfeusz morfeusz) {
        for(MorphInterpretation interpretation: morfeusz.analyseAsList(word)) {
            if(lemma.equals(interpretation.getLemma())) {
                return interpretation;
            }
        }
        return null;
    }

    public static List<MorphInterpretation> getSingleWordInterpretations(List<MorphInterpretation> morphInterpretationList, int position) {
        MorphInterpretation currentItem = morphInterpretationList.get(position);

        int beginIndex = findPreviousWord(morphInterpretationList, position);
        int endIndex = findNextWord(morphInterpretationList, position);
        return morphInterpretationList.subList(beginIndex+1, endIndex == -1 ? morphInterpretationList.size() : endIndex + 1);
    }

    public static int findNextWord(List<MorphInterpretation> morphInterpretationList, int position) {
        MorphInterpretation currentItem = morphInterpretationList.get(position);
        String orth = currentItem.getOrth();
        String currentOrth = orth;

        int i = position;
        while(orth.equals(currentOrth)) {
            ++i;
            if(i >= morphInterpretationList.size()) {
                return -1;
            }
            currentItem = morphInterpretationList.get(i);
            currentOrth = currentItem.getOrth();
        }
        return i;
    }

    public static int findPreviousWord(List<MorphInterpretation> morphInterpretationList, int position) {
        MorphInterpretation currentItem = morphInterpretationList.get(position);
        String orth = currentItem.getOrth();
        String currentOrth = orth;

        int i = position;
        while(orth.equals(currentOrth)) {
            --i;
            if(i < 0) {
                return -1;
            }
            currentItem = morphInterpretationList.get(i);
            currentOrth = currentItem.getOrth();
        }
        return i;
    }

    public static String getType(MorphInterpretation interpretation, Morfeusz morfeusz) {
        String[] wordInfo = morfeusz.getIdResolver().getTag(interpretation.getTagId()).split(":");
        return wordInfo.length > 0 ? wordInfo[0] : null;
    }

}
