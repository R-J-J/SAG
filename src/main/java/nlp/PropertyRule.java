package nlp;

import pl.sgjp.morfeusz.Morfeusz;
import pl.sgjp.morfeusz.MorphInterpretation;
import utils.Constants;

import java.util.List;

/**
 * Created by Arjan on 05.06.2016.
 */
public class PropertyRule extends Rule {

    private final Morfeusz morfeusz;

    public PropertyRule(AnalysisBuilder analysisBuilder, Morfeusz morfeusz) {
        super(analysisBuilder);
        this.morfeusz = morfeusz;
    }

    @Override
    public void checkRule(List<MorphInterpretation> results, int position) {
        if(results.size() < position+2) {
            return;
        }

        MorphInterpretation object = results.get(position);

        int nextWordIndex = LanguageUtils.findNextWord(results, position);

        List<MorphInterpretation> nextWordInterpretations = LanguageUtils.getSingleWordInterpretations(results, nextWordIndex);
        MorphInterpretation nextWord = LanguageUtils.findByType(nextWordInterpretations, morfeusz, LanguageUtils.NOUN, LanguageUtils.ADJECTIVE);

        int previousWordIndex = LanguageUtils.findPreviousWord(results, position);

        List<MorphInterpretation> previousWordInterpretations = LanguageUtils.getSingleWordInterpretations(results, previousWordIndex);
        MorphInterpretation previousWord = LanguageUtils.findByType(previousWordInterpretations, morfeusz, LanguageUtils.NOUN, LanguageUtils.ADJECTIVE);

        String previousType = previousWord == null ? null : LanguageUtils.getType(previousWord, morfeusz);
        String nextType = nextWord == null ? null : LanguageUtils.getType(nextWord, morfeusz);
        if(previousType != null) {
           //pies burek, psa burka
           if (LanguageUtils.NOUN.equals(previousType) && isSameDeclination(previousWord, object, morfeusz)) {
                analysisBuilder.addSubclass(new Subclass(previousWord.getLemma(), object.getLemma()));
            }
            //czarny burek
            if (LanguageUtils.ADJECTIVE.equals(previousType)) {
                analysisBuilder.addDataProperty(new Property(object.getLemma(), Constants.HAS_ATTRIBUTE, previousWord.getLemma()));
            }
        }
        if(nextType != null) {
            //burek pies?
            if (LanguageUtils.NOUN.equals(nextType) && isSameDeclination(nextWord, object, morfeusz)) {
                analysisBuilder.addObjectProperty(new Property(object.getLemma(), Constants.IS_ASSOCIATED_WITH, nextWord.getLemma()));
            }
            //pole siłowe, partia polityczna
            if (LanguageUtils.ADJECTIVE.equals(nextType)) {
                analysisBuilder.addSubclass(new Subclass(object.getLemma(), object.getOrth()+" "+nextWord.getOrth()));
            }
        }
    }

    private boolean isSameDeclination(MorphInterpretation word1, MorphInterpretation word2, Morfeusz morfeusz) {
        String declination1 = LanguageUtils.getDeclination(word1, morfeusz);
        return declination1 != null && declination1.equals(LanguageUtils.getDeclination(word2, morfeusz));
    }

}
