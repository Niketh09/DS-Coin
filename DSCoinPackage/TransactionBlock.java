package DSCoinPackage;

import HelperClasses.MerkleTree;
import HelperClasses.CRF;

public class TransactionBlock {

  public Transaction[] trarray;
  public TransactionBlock previous;
  public MerkleTree Tree;
  public String trsummary;
  public String nonce;
  public String dgst;

  TransactionBlock(Transaction[] t) {
    Transaction[] garden = new Transaction[t.length];
    for(int i=0;i<t.length;i++){
      garden[i] = t[i];
    }
    this.trarray = garden;
    this.previous = null;
    this.Tree = new MerkleTree();
		String s = this.Tree.Build(t);
    this.trsummary = s;
    this.dgst = null;
    this.nonce = null;
  }

  public boolean checkTransaction (Transaction t) {
    TransactionBlock temp = t.coinsrc_block;
    TransactionBlock current = this;
    Transaction source = null;
    int index = 0;
    if(temp==null){
      return true;
    }
    for(int i=0;i<temp.trarray.length;i++){
      if(t.coinID.equals(temp.trarray[i].coinID)&&temp.trarray[i].Destination.UID.equals(t.Source.UID)){
        source = temp.trarray[i];
        index = i;
      }
    }
    if(source==null){
      return false;
    }
    for(int i=index+1;i<temp.trarray.length;i++){
      if(t.coinID.equals(temp.trarray[i].coinID)){
        return false;
      }
    }
    for(int i=0;i<this.trarray.length;i++){
      if(this.trarray[i]==t){
        index = i;
      }
    }
    for(int i=0;i<index;i++){
      if(this.trarray[i].coinID.equals(t.coinID)){
        return false;
      }
    }
    while(current!=temp){
      for(int i=0;i<current.trarray.length;i++){
        if(current.trarray[i].coinID.equals(t.coinID)){
          return false;
        }
      }
      current = current.previous;
    }
    return true;
  }
}
