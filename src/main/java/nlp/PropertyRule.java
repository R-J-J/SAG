package nlp;

import com.google.common.collect.Sets;
import pl.sgjp.morfeusz.Morfeusz;
import pl.sgjp.morfeusz.MorphInterpretation;

import java.util.List;
import java.util.Set;

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
        if(nextWordIndex == -1) {
            return;
        }

        List<MorphInterpretation> nextWordInterpretations = LanguageUtils.getSingleWordInterpretations(results, nextWordIndex);
        MorphInterpretation nextWord = LanguageUtils.findByType(nextWordInterpretations, morfeusz, LanguageUtils.NOUN, LanguageUtils.ADJECTIVE);

        int previousWordIndex = LanguageUtils.findPreviousWord(results, position);
        if(previousWordIndex == -1) {
            return;
        }

        List<MorphInterpretation> previousWordInterpretations = LanguageUtils.getSingleWordInterpretations(results, nextWordIndex);
        MorphInterpretation previousWord = LanguageUtils.findByType(previousWordInterpretations, morfeusz, LanguageUtils.NOUN, LanguageUtils.ADJECTIVE);

        String previousType = previousWord == null ? null : LanguageUtils.getType(previousWord, morfeusz);
        String nextType = nextWord == null ? null : LanguageUtils.getType(nextWord, morfeusz);
        if(previousType != null) {
           //pies burek
           if (LanguageUtils.NOUN.equals(previousType)) {
                analysisBuilder.addObjectSubclass(new ObjectSubclass(object.getLemma(), previousWord.getLemma()));
            }
            //czarny burek
            if (LanguageUtils.ADJECTIVE.equals(previousType)) {
                analysisBuilder.addObjectProperty(new ObjectProperty(object.getLemma(), previousWord.getLemma()));
            }
        }
        if(nextType != null) {
            //burek pies? prezes partii?
            if (LanguageUtils.NOUN.equals(nextType)) {
                analysisBuilder.addObjectProperty(new ObjectProperty(nextWord.getLemma(), object.getLemma()));
            }
            //burek czarny? kundel bury? coś bez sensu...
            if (LanguageUtils.ADJECTIVE.equals(nextType)) {
                analysisBuilder.addObjectProperty(new ObjectProperty(object.getLemma(), nextWord.getLemma()));
            }
        }
    }

}
