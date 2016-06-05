package nlp;

import pl.sgjp.morfeusz.MorphInterpretation;

import java.util.List;

/**
 * Created by Arjan on 05.06.2016.
 */
public abstract class Rule {

    protected final AnalysisBuilder analysisBuilder;

    public Rule(AnalysisBuilder analysisBuilder) {
        this.analysisBuilder = analysisBuilder;
    }

    public abstract void checkRule(List<MorphInterpretation> results, int position);

}
