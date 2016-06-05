package nlp;

import com.google.common.collect.Sets;
import pl.sgjp.morfeusz.MorphInterpretation;

import java.util.*;

/**
 * Created by Arjan on 05.06.2016.
 */
public class SubclassRule extends Rule {

    private static final Set<String> subClassPhrases = Sets.newHashSet("być", "zostać");
    private static final String TRUE = "true";

    public SubclassRule(AnalysisBuilder analysisBuilder) {
        super(analysisBuilder);
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
        MorphInterpretation verb = results.get(verbIndex);
        if(!subClassPhrases.contains(verb.getLemma())) {
            return;
        }
        int subclassIndex = LanguageUtils.findNextWord(results, verbIndex);
        if(subclassIndex == -1) {
            return;
        }
        MorphInterpretation subclass = results.get(subclassIndex);
        analysisBuilder.addObjectSubclass(new ObjectSubclass(object.getLemma(), subclass.getLemma()));
    }

}
