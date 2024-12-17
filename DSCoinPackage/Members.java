package DSCoinPackage;

import java.util.*;
import HelperClasses.Pair;

public class Members
 {

  public String UID;
  public List<Pair<String, TransactionBlock>> mycoins;
  public Transaction[] in_process_trans;
  int transcount = 0;

  public void initiateCoinsend(String destUID, DSCoin_Honest DSobj) {
    String coinval = mycoins.get(0).first;
    TransactionBlock temp = mycoins.get(0).second;
    mycoins.remove(0);
    Transaction tobj = new Transaction();
    tobj.coinID = coinval;
    tobj.Source = this;
    tobj.coinsrc_block = temp;
    for(int i=0;i<DSobj.memberlist.length;i++){
      if(DSobj.memberlist[i].UID.equals(destUID)){
        tobj.Destination = DSobj.memberlist[i];
        break;
      }
    }
    in_process_trans[transcount] = tobj;
    transcount = transcount+1;
    DSobj.pendingTransactions.AddTransactions(tobj);
  }

  public Pair<List<Pair<String, String>>, List<Pair<String, String>>> finalizeCoinsend (Transaction tobj, DSCoin_Honest DSObj) throws MissingTransactionException {
    TransactionBlock temp = DSObj.bChain.lastBlock;
    int k = 0;
    int totalnodes = temp.trarray.length;
    boolean flag = false;
    while(temp!=null){
      for(int i=0;i<temp.trarray.length;i++){
        if(tobj==temp.trarray[i]){
          flag = true;
          k = i;
          break;
        }
      }
      if(flag==true){
        break;
      }
      temp = temp.previous;
    }
    if(flag==false){
      throw new MissingTransactionException();
    }
    List<Pair<String,String>> A = new ArrayList<Pair<String,String>>();
    List<Pair<String,String>> B = new ArrayList<Pair<String,String>>();
    if(temp.trarray.length==1){
      A.add(new Pair<String,String>(temp.Tree.rootnode.val,null));
    }
    else{
      int power = 0;
      while(totalnodes!=1){
        power++;
        totalnodes = totalnodes/2;
      }
      A = temp.Tree.sibcoupath(k,power);
    }
    TransactionBlock iter = DSObj.bChain.lastBlock;
    while(iter!=temp){
      B.add(new Pair<String,String>(iter.dgst,iter.previous.dgst+"#"+iter.trsummary+"#"+iter.nonce));
      iter = iter.previous;
    }
    if(temp.previous!=null){
      B.add(new Pair<String,String>(temp.dgst,temp.previous.dgst+"#"+temp.trsummary+"#"+temp.nonce));
      B.add(new Pair<String,String>(temp.previous.dgst,null));
    }
    else if(temp.previous==null){
      B.add(new Pair<String,String>(temp.dgst,"DSCoin"+"#"+temp.trsummary+"#"+temp.nonce));
      B.add(new Pair<String,String>("DSCoin",null));
    }
    List<Pair<String,String>> C = new ArrayList<Pair<String,String>>();
    for(int i=B.size()-1;i>=0;i--){
      C.add(new Pair<String,String>(B.get(i).first,B.get(i).second));
    }
    boolean val = false;
    for(int i=0;i<in_process_trans.length;i++){
      if(in_process_trans[i]==tobj){
        val = true;
      }
      if(val){
        if(in_process_trans[i]!=null){
          in_process_trans[i]=in_process_trans[i+1];
        }
        else{
          break;
        }
      }
    }
    List<Pair<String,TransactionBlock>> newlist = new ArrayList<Pair<String,TransactionBlock>>();
    int inc = 0;
    for(int i=0;i<tobj.Destination.mycoins.size();i++){
      if(tobj.Destination.mycoins.get(i).first.compareTo(tobj.coinID)<0){
        newlist.add(new Pair<String,TransactionBlock>(tobj.Destination.mycoins.get(i).first,tobj.Destination.mycoins.get(i).second));
      }
      else if(tobj.Destination.mycoins.get(i).first.compareTo(tobj.coinID)>0){
        if(inc<1){
          newlist.add(new Pair<String,TransactionBlock>(tobj.coinID,temp));
          inc++;
        }
        newlist.add(new Pair<String,TransactionBlock>(tobj.Destination.mycoins.get(i).first,tobj.Destination.mycoins.get(i).second));
      }
    }
    tobj.Destination.mycoins = newlist;
    Pair<List<Pair<String,String>>, List<Pair<String,String>>> D = new Pair<List<Pair<String,String>>, List<Pair<String,String>>>(A,C);
    return D;
  }

  public void MineCoin(DSCoin_Honest DSObj) throws EmptyQueueException{
    Transaction[] array = new Transaction[DSObj.bChain.tr_count];
    Transaction invalid = null;
    int l = 0;
    int invalidtrans = 0;
    boolean inqueuetrans = true;
    if(l!=0){
      int m = 0;
      while(array[m]!=null){
        if(array[m].coinID.equals(DSObj.pendingTransactions.firstTransaction.coinID)){
          inqueuetrans = false;
          break;
        }
        m++;
      }
    }
    while(l!=DSObj.bChain.tr_count-1){
      if(DSObj.bChain.lastBlock.checkTransaction(DSObj.pendingTransactions.firstTransaction)&&inqueuetrans){
        array[l] = DSObj.pendingTransactions.RemoveTransaction();
        l++;
      }
      else{
        invalid = DSObj.pendingTransactions.RemoveTransaction();
        invalidtrans++;
      }
    }
    Transaction minorreward = new Transaction();
    int k = Integer.parseInt(DSObj.latestCoinID);
    k++;
    DSObj.latestCoinID = String.valueOf(k);
    minorreward.coinID = DSObj.latestCoinID;
    minorreward.Source = null;
    minorreward.coinsrc_block = null;
    minorreward.Destination = this;
    array[DSObj.bChain.tr_count-1] = minorreward;
    TransactionBlock newblock = new TransactionBlock(array);
    DSObj.bChain.InsertBlock_Honest(newblock);
    this.mycoins.add(new Pair<String,TransactionBlock>(minorreward.coinID,newblock));
  }  

  public void MineCoin(DSCoin_Malicious DSObj) throws EmptyQueueException{
    Transaction[] array = new Transaction[DSObj.bChain.tr_count];
    Transaction invalid = null;
    int l = 0;
    int invalidtrans = 0;
    boolean inqueuetrans = true;
    if(l!=0){
      int m = 0;
      while(array[m]!=null){
        if(array[m].coinID.equals(DSObj.pendingTransactions.firstTransaction.coinID)){
          inqueuetrans = false;
          break;
        }
        m++;
      }
    }
    while(l!=DSObj.bChain.tr_count-1){
      if(DSObj.bChain.lastBlocksList[0].checkTransaction(DSObj.pendingTransactions.firstTransaction)&&inqueuetrans){
        array[l] = DSObj.pendingTransactions.RemoveTransaction();
        l++;
      }
      else{
        invalid = DSObj.pendingTransactions.RemoveTransaction();
        invalidtrans++;
      }
    }
    Transaction minorreward = new Transaction();
    int k = Integer.parseInt(DSObj.latestCoinID);
    k++;
    DSObj.latestCoinID = String.valueOf(k);
    minorreward.coinID = DSObj.latestCoinID;
    minorreward.Source = null;
    minorreward.coinsrc_block = null;
    minorreward.Destination = this;
    array[DSObj.bChain.tr_count-1] = minorreward;
    TransactionBlock newblock = new TransactionBlock(array);
    DSObj.bChain.InsertBlock_Malicious(newblock);
    this.mycoins.add(new Pair<String,TransactionBlock>(minorreward.coinID,newblock));
  }  
}
