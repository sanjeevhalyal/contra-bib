package contrabib;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.flows.*;
import net.corda.core.transactions.SignedTransaction;

@InitiatedBy(ItemIssueFlowInitiator.class)
public class ItemIssueFlowResponder extends FlowLogic<Void> {

    private final FlowSession otherSide;

    public ItemIssueFlowResponder(FlowSession otherSide) {
        this.otherSide = otherSide;
    }

    @Override
    @Suspendable
    public Void call() throws FlowException {
        SignedTransaction signedTransaction = subFlow(new SignTransactionFlow(otherSide) {
            @Suspendable
            @Override
            protected void checkTransaction(SignedTransaction stx) throws FlowException {}
        });
        subFlow(new ReceiveFinalityFlow(otherSide, signedTransaction.getId()));
        return null;
    }
}
