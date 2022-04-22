package sorcer.sml.regions;

import sml.builder.MuiltidisciplinaryBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sorcer.test.ProjectContext;
import org.sorcer.test.SorcerTestRunner;
import sorcer.arithmetic.provider.impl.*;
import sorcer.core.invoker.Pipeline;
import sorcer.core.service.Governance;
import sorcer.service.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static sorcer.co.operator.*;
import static sorcer.ent.operator.*;
import static sorcer.eo.operator.args;
import static sorcer.eo.operator.*;
import static sorcer.eo.operator.fi;
import static sorcer.eo.operator.loop;
import static sorcer.eo.operator.result;
import static sorcer.mo.operator.*;
import static sorcer.mo.operator.out;
import static sorcer.mo.operator.value;
import static sorcer.so.operator.*;

/**
 * Created by Mike Sobolewski on 03/11/2021.
 */
@RunWith(SorcerTestRunner.class)
@ProjectContext("examples/sml")
public class Nodes {

    private final static Logger logger = LoggerFactory.getLogger(Nodes.class);

    @Test
    public void opservicePipeline() throws Exception {

        Opservice lambdaOut = invoker("lambdaOut",
            (Context<Double> cxt) -> value(cxt, "x") + value(cxt, "y") + 30,
            args("x", "y"));

        Opservice exprOut = invoker("exprOut", "lambdaOut - y", args("lambdaOut", "y"));

        Opservice sigOut = sig("multiply", MultiplierImpl.class,
            result("z", inPaths("lambdaOut", "exprOut")));

        Pipeline opspl = pl(
            lambdaOut,
            exprOut,
            sigOut);

        setContext(opspl, context("mfpcr",
            inVal("x", 20.0),
            inVal("y", 80.0)));

        Context out = (Context) exec(opspl);

        logger.info("pipeline: " + out);
        assertEquals(130.0, value(out, "lambdaOut"));
        assertEquals(50.0, value(out, "exprOut"));
        assertEquals(6500.0, value(out, "z"));
    }

    @Test
    public void conditionalPipelineDiscipline() throws Exception {

        Opservice lambdaOut = invoker("lambdaOut",
            (Context<Double> cxt) -> value(cxt, "lambdaOut") + value(cxt, "x") + value(cxt, "y") + 10,
            args("x", "y"));

        Opservice exprOut = invoker("exprOut", "lambdaOut - y", args("lambdaOut", "y"));

        Opservice sigOut = sig("multiply", MultiplierImpl.class,
            result("z", inPaths("lambdaOut", "exprOut")));

        Pipeline opspl = pl("cxtn1",
            lambdaOut,
            exprOut,
            sigOut);

        // cxtn1 is a free contextion for a discipline dispatcher
        Block plDispatch = block(
            loop(condition(cxt -> (double)
                value(cxt, "lambdaOut") < 500.0), pipeline("cxtn1")));

        Node plDis = rnd("pln-nd",
            rndFi("pln-nd", cxtnFi("cxtn1", opspl), dspFi("dspt1", plDispatch)));

        setContext(opspl, context("mfpcr",
            inVal("lambdaOut", 20.0),
            inVal("x", 20.0),
            inVal("y", 80.0)));

        // out is the discipline output
        Context out  = eval(plDis);

        logger.info("pipeline out: " + out);
        assertEquals(570.0, value(out, "lambdaOut"));
        assertEquals(490.0, value(out, "exprOut"));
        assertEquals(279300.0, value(out, "multiply"));
    }

    @Test
    public void morphModelDiscipline() throws Exception {

        // evaluate a discipline specified by a signature
        Context out  = eval(sig("getMorphModelDiscipline", MuiltidisciplinaryBuilder.class));

        logger.info("morphModelDiscipline: " + out);
        assertTrue(value(out, "morpher3").equals(920.0));
    }

    @Test
    public void multiFiPipelineDisciplineFi_plDisc1() throws Exception {

        Signature discSig = sig("getMultiFiPipelineDiscipline",
            MuiltidisciplinaryBuilder.class);

        // first fidelity
        Context out = eval(discSig);

        logger.info("pipeline: " + out);

        assertEquals(20.0, value(out, "x"));
        assertEquals(80.0, value(out, "y"));
        assertEquals(2000.0, value(out, "multiply"));
        assertEquals(100.0, value(out, "lambdaOut"));
        assertEquals(20.0, value(out, "exprOut"));
    }

    @Test
    public void multiFiPipelineyDiscipline_plDisc2() throws Exception {

        Signature discSig = sig("getMultiFiPipelineDiscipline",
            MuiltidisciplinaryBuilder.class);

        // first fidelity
        Context out = eval(discSig, fi("plDisc2"));

        logger.info(" pipeline: " + out);

        assertEquals(20.0, value(out, "x"));
        assertEquals(80.0, value(out, "y"));
        assertEquals(228800.0, value(out, "multiply"));
        assertEquals(520.0, value(out, "lambdaOut"));
        assertEquals(440.0, value(out, "exprOut"));
    }

}
