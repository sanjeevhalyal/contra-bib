package contrabib;

import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.CommandWithParties;
import net.corda.core.contracts.Contract;
import net.corda.core.contracts.ContractState;
import net.corda.core.transactions.LedgerTransaction;

import static net.corda.core.contracts.ContractsDSL.requireSingleCommand;
import static net.corda.core.contracts.ContractsDSL.requireThat;

import java.util.List;

public class ItemContract implements Contract {
    public static String ID = "contrabib.ItemContract";


    public void verify(LedgerTransaction tx) throws IllegalArgumentException {


        List<ContractState> inputs = tx.getInputStates();
        List<ContractState> outputs = tx.getOutputStates();

        List<CommandWithParties<CommandData>> commands = tx.getCommands();

        if (commands.size() != 1) throw new IllegalArgumentException("tx should have only one command");
        if (!(commands.get(0).getValue() instanceof ItemContract.Commands.Issue))
            throw new IllegalArgumentException("Must be an issue command");

        CommandData theCommand = commands.get(0).getValue();


        //Shape of the transaction
        if (inputs.size() != 0) throw new IllegalArgumentException("must have zero inputs");
        if (outputs.size() != 1) throw new IllegalArgumentException("must have one output");

        if (!(outputs.get(0) instanceof ItemState))
            throw new IllegalArgumentException("output must be of type ItemState");

        ItemState itemState = (ItemState) outputs.get(0);


        //required signers
        if (!(commands.get(0).getSigners().contains(itemState.getIssuer().getOwningKey()))) throw new IllegalArgumentException("Issuer must be required signer");
    }


    public interface Commands extends CommandData {
        class Issue implements Commands {
        }
    }
}
