package contrabib;

import net.corda.core.contracts.BelongsToContract;
import net.corda.core.contracts.ContractState;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* Our state, defining a shared fact on the ledger.
 * See src/main/java/examples/ArtState.java for an example. */
@BelongsToContract(ItemContract.class)
public class ItemState implements ContractState {

    private  Party issuer;

    private  Party owner;

    private String name;
    private String id;
    private String status;

    public ItemState(Party issuer, Party owner, String name, String id, String status) {
        this.issuer = issuer;
        this.owner = owner;
        this.name = name;
        this.id = id;
        this.status = status;
    }

    public Party getIssuer() {
        return issuer;
    }

    public Party getOwner() {
        return owner;
    }


    public void setIssuer(Party issuer) {
        this.issuer = issuer;
    }

    public void setOwner(Party owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @NotNull
    @Override
    public List<AbstractParty> getParticipants() {
        return Arrays.asList(issuer,owner);
    }
}
