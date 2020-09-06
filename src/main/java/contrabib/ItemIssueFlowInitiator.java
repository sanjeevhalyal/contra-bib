package contrabib;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.flows.*;
import net.corda.core.identity.Party;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;
import net.corda.core.utilities.ProgressTracker;
import net.corda.core.contracts.CommandData;

import static java.util.Collections.singletonList;

@InitiatingFlow
@StartableByRPC
public class ItemIssueFlowInitiator extends FlowLogic<SignedTransaction> {
    private final Party owner;
    private final String name;
    private final String id;
    private final String status;

    public ItemIssueFlowInitiator(Party owner, String name, String id, String status) {
        this.owner = owner;
        this.name = name;
        this.id = id;
        this.status = status;
    }

    private final ProgressTracker progressTracker = new ProgressTracker();

    @Override
    public ProgressTracker getProgressTracker() {
        return progressTracker;
    }

    @Suspendable
    @Override
    public SignedTransaction call() throws FlowException {
        Party notary = getServiceHub().getNetworkMapCache().getNotaryIdentities().get(0);
        Party issuer = getOurIdentity();

        ItemState itemState = new ItemState(issuer, owner, name, id, status);
        CommandData commandData = new ItemContract.Commands.Issue();

        TransactionBuilder transactionBuilder = new TransactionBuilder(notary);
        transactionBuilder.addOutputState(itemState, ItemContract.ID);
        transactionBuilder.addCommand(commandData, issuer.getOwningKey(), owner.getOwningKey());

        transactionBuilder.verify(getServiceHub());

        FlowSession session = initiateFlow(owner);

        SignedTransaction signedTransaction = getServiceHub().signInitialTransaction(transactionBuilder);

        SignedTransaction fullySignedTransaction = subFlow(new CollectSignaturesFlow(signedTransaction, singletonList(session)));

        return subFlow(new FinalityFlow(fullySignedTransaction, singletonList(session)));
    }
}
