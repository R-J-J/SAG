package nlp;

import com.google.common.collect.Sets;
import pl.sgjp.morfeusz.Morfeusz;
import pl.sgjp.morfeusz.MorphInterpretation;

import java.util.*;

/**
 * Created by Arjan on 05.06.2016.
 */
public class IsRule extends Rule {

    private static final Set<String> isLemmas = Sets.newHashSet("być", "zostać");

    private final Morfeusz morfeusz;

    public IsRule(AnalysisBuilder analysisBuilder, Morfeusz morfeusz) {
        super(analysisBuilder);
        this.morfeusz = morfeusz;
    }

    @Override
    public void checkRule(List<MorphInterpretation> results, int position) {
        if(results.size() < position+2) {
            return;
        }

        MorphInterpretation object = results.get(position);

        int verbIndex = LanguageUtils.findNextWord(results, position);
        if(verbIndex == -1) {
            return;
        }

        List<MorphInterpretation> verbInterpretations = LanguageUtils.getSingleWordInterpretations(results, verbIndex);
        MorphInterpretation verb = LanguageUtils.findByType(verbInterpretations, morfeusz, "fin", "bedzie", "praet");
        if(verb == null) {
            return;
        }

        if(!isLemmas.contains(verb.getLemma())) {
            return;
        }

        int subclassIndex = LanguageUtils.findNextWord(results, verbIndex);
        if(subclassIndex == -1) {
            return;
        }
        List<MorphInterpretation> subclassInterpretations = LanguageUtils.getSingleWordInterpretations(results, subclassIndex);
        MorphInterpretation subclass = LanguageUtils.findByType(subclassInterpretations, morfeusz, LanguageUtils.NOUN, LanguageUtils.ADJECTIVE);
        if(subclass == null) {
            return;
        }

        String type = LanguageUtils.getType(subclass, morfeusz);
        if(LanguageUtils.NOUN.equals(type)) {
            analysisBuilder.addObjectSubclass(new ObjectSubclass(object.getLemma(), subclass.getLemma()));
        }
        if(LanguageUtils.ADJECTIVE.equals(type)) {
            analysisBuilder.addObjectProperty(new ObjectProperty(object.getLemma(), subclass.getLemma()));
        }
    }

}
