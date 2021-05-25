/*
 * Copyright 2021 the original author or authors.
 * Copyright 2021 SorcerSoft.org.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package sorcer.core.service;

import net.jini.core.transaction.Transaction;
import sorcer.core.context.DesignContext;
import sorcer.core.context.ModelStrategy;
import sorcer.core.context.ServiceContext;
import sorcer.core.context.model.ent.Analyzer;
import sorcer.core.context.model.ent.Developer;
import sorcer.core.signature.LocalSignature;
import sorcer.service.*;
import sorcer.service.modeling.ExecutiveException;
import sorcer.service.modeling.Finalization;
import sorcer.service.modeling.Initialization;

import java.rmi.RemoteException;
import java.util.List;

import static sorcer.mo.operator.devFi;

/**
 * A top-level interface for disciplinary transdesigns.
 *
 * @author Mike Sobolewski 05/24/2021
 */
public class Transdesign extends MultiFiSlot implements Design {


    // transdiscipline
    private Discipline discipline;

    private Context disciplineIntent;

    private Context developmentIntent;

    private Context designIntent;

    private Fidelity<Development> developerFi;

    // the output of this collaboration
    protected Context output;

    protected Fidelity<Finalization> finalizerFi;

    protected Fidelity<Initialization> initializerFi;

    public void setDeveloperFi(Fidelity<Development> developerFi) {
        this.developerFi = developerFi;
    }

    protected ServiceStrategy serviceStrategy;

    private static int count = 0;

    public Transdesign(String name) {
        if (name == null) {
            this.key = getClass().getSimpleName() + "-" + count++;
        } else {
            this.key = name;
        }
        serviceStrategy = new ModelStrategy(this);
    }

    public Transdesign(String name, Context designIntent) throws SignatureException {
        this(name);
        this.designIntent = designIntent;
        DesignContext dznCxt = (DesignContext)designIntent;
        Signature discSig = dznCxt.getDisciplineSignature();
        Signature discIntentSig = dznCxt.getDisciplineIntentSignature();
        if (discSig != null) {
            discipline = (Discipline) ((LocalSignature)discSig).initInstance();
        } else {
            discipline = dznCxt.getDiscipline();
        }

        if (discIntentSig != null) {
            disciplineIntent = (Context) ((LocalSignature)discIntentSig).initInstance();
        } else {
            disciplineIntent = dznCxt.getDisciplineIntent();
        }
        developerFi = dznCxt.getDeveloperFi();
        if (developerFi == null) {
            setDeveloperFi(designIntent);
        }
        developmentIntent = dznCxt.getDevelopmentIntent();
    }

    public Transdesign(String name, Discipline discipline, Context disciplineIntent, Development developer ) {
        this(name);
        this.discipline = discipline;
        this.disciplineIntent = disciplineIntent;
        developerFi = new Fidelity(name);
        developerFi.addSelect(developer);
        developerFi.setSelect(developer);
        ((Developer)developer).setDiscipline(discipline);
    }

    public Fidelity<Development> setDeveloperFi(Context context) {
        if(developerFi == null) {
            Object devComponent = context.get(Context.DEV_PATH);
            if (devComponent != null) {
                if (devComponent instanceof Development) {
                    developerFi = new Fidelity(((Developer)devComponent).getName());
                    developerFi.addSelect((Development) devComponent);
                    developerFi.setSelect((Development)devComponent);
                    ((Developer)devComponent).setDiscipline(discipline);
                } else if (devComponent instanceof ServiceFidelity
                    && ((ServiceFidelity) devComponent).getFiType().equals(Fi.Type.DEV)) {
                    developerFi = (Fidelity) devComponent;
                    ((Developer)developerFi.getSelect()).setDiscipline(discipline);
                }
            }
        }
        ((ServiceContext)context).getDomainStrategy().setExecState(Exec.State.INITIAL);
        if (output == null) {
            output = new ServiceContext(getName());
        }
        return developerFi;
    }

    public Context getDesignIntent() {
        return designIntent;
    }

    public void setDesignIntent(Context designIntent) {
        this.designIntent = designIntent;
    }

    @Override
    public Context evaluate(Context context, Arg... args) throws MogramException, RemoteException, ServiceException {
        return discipline.evaluate(context, args);
    }

    @Override
    public <T extends Contextion> T exert(Transaction txn, Arg... args) throws RemoteException, ServiceException {
         return (T) discipline.evaluate(disciplineIntent, args);
    }

    @Override
    public String getDomainName() {
        return (String) key;
    }

    @Override
    public Context getOutput(Arg... args) throws ContextException {
        return output;
    }

    public Context getDisciplineIntent() {
        return disciplineIntent;
    }

    public void setDisciplineIntent(Context disciplineIntent) {
        this.disciplineIntent = disciplineIntent;
    }

    @Override
    public void setContext(Context input) throws ContextException {
        developmentIntent = input;
    }

    @Override
    public Context appendContext(Context context) throws ContextException, RemoteException {
        return developmentIntent;
    }

    @Override
    public Context getDomainData() throws ContextException, RemoteException {
        return disciplineIntent;
    }

    @Override
    public Context getContext(Context contextTemplate) throws RemoteException, ContextException {
        return null;
    }

    @Override
    public Context appendContext(Context context, String path) throws ContextException, RemoteException {
        return null;
    }

    @Override
    public Context getContext(String path) throws ContextException, RemoteException {
        return null;
    }

    @Override
    public boolean isExec() {
        return false;
    }

    @Override
    public ServiceStrategy getDomainStrategy() {
        return null;
    }

    @Override
    public Projection getInPathProjection() {
        return null;
    }

    @Override
    public Projection getOutPathProjection() {
        return null;
    }

    @Override
    public List<Contextion> getContextions(List<Contextion> contextionList) {
        contextionList.add(discipline);
        return contextionList;
    }

    @Override
    public Context getContext() throws ContextException {
        return developmentIntent;
    }

    @Override
    public Discipline getDiscipline() throws RemoteException {
        return discipline;
    }

    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
    }

    @Override
    public Fidelity<Development> getDeveloperFi() throws RemoteException {
        return developerFi;
    }

    @Override
    public Context design(Discipline discipline, Context context) throws DesignException, RemoteException {
       this.discipline = discipline;
        try {
            return developerFi.getSelect().develop(discipline, developmentIntent);
        } catch (ServiceException | ExecutiveException e) {
            throw new DesignException(e);
        }
    }

    @Override
    public Fidelity<Initialization> getInitializerFi() {
        return initializerFi;
    }

    @Override
    public Fidelity<Finalization> getFinalizerFi() {
        return finalizerFi;
    }

    @Override
    public void setName(String name) {
        key = name;
    }
}