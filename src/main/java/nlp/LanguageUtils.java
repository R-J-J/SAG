package nlp;

import pl.sgjp.morfeusz.Morfeusz;
import pl.sgjp.morfeusz.MorphInterpretation;

/**
 * Created by Arjan on 05.06.2016.
 */
public abstract class LanguageUtils {

    public static final String NOUN = "substr";
    public static final String ADJECTIVE = "adj";

    private LanguageUtils() {

    }

    public static MorphInterpretation findBasicNoun(String word, Morfeusz morfeusz) {
        return findByType(word, NOUN, morfeusz);
    }

    public static MorphInterpretation findByType(String word, String type, Morfeusz morfeusz) {
        for(MorphInterpretation interpretation: morfeusz.analyseAsList(word)) {
            if(type.equals(morfeusz.getIdResolver().getTag(interpretation.getTagId()))) {
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


}
