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

    public static MorphInterpretation findBasicNoun(String word, Morfeusz morfeusz) {
        return findByType(word, NOUN, morfeusz);
    }

    public static MorphInterpretation findByType(String word, String type, Morfeusz morfeusz) {
        for(MorphInterpretation interpretation: morfeusz.analyseAsList(word)) {
            String foundType = morfeusz.getIdResolver().getTag(interpretation.getTagId());
            if(type.equals(foundType.split(":")[0])) {
                return interpretation;
            }
        }
        return null;
    }

    public static MorphInterpretation findByLemma(String word, String lemma, Morfeusz morfeusz) {
        for(MorphInterpretation interpretation: morfeusz.analyseAsList(word)) {
            if(lemma.equals(interpretation.getLemma())) {
                return interpretation;
            }
        }
        return null;
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

}
