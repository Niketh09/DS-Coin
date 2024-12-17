package DSCoinPackage;
import HelperClasses.Pair;
public class Moderator
 {

  public void initializeDSCoin(DSCoin_Honest DSObj, int coinCount) {
    int x = 99999;
    Members mod = new Members();
    int k = DSObj.memberlist.length;
    int a = coinCount/k;
    mod.UID = "Moderator";
    Transaction[] niketh = new Transaction[coinCount];
    int length = 0;
    for(int i=0;i<coinCount/k;i++){
      for(int j=0;j<k;j++){
        x++;
        DSObj.latestCoinID = String.valueOf(x);
        Transaction t = new Transaction();
        t.coinID = String.valueOf(x);
        t.Source = mod;
        t.Destination = DSObj.memberlist[j];
        t.coinsrc_block = null;
        niketh[i*k+j] = t;
      }
    }
    int count=0;
    Transaction[] tarray = new Transaction[DSObj.bChain.tr_count];
    TransactionBlock[] blockarray = new TransactionBlock[a];
    int numblocks = 0;
    for(int i=0;i<coinCount;i++){
      tarray[count] = niketh[i];
      count++;
      if(count==DSObj.bChain.tr_count){
        count = 0;
        TransactionBlock block = new TransactionBlock(tarray);
        DSObj.bChain.InsertBlock_Honest(block);
        blockarray[numblocks] = block;
        numblocks = numblocks+1;
      }
    }
    int global = 0;
    for(int i=0;i<numblocks;i++){
      for(int j=0;j<DSObj.bChain.tr_count;j++){
        if(global==k){
          global = 0;
        }
        DSObj.memberlist[global].mycoins.add(new Pair<String,TransactionBlock>(blockarray[i].trarray[j].coinID,blockarray[i]));
        global++;
      }
    }
  }
    
  public void initializeDSCoin(DSCoin_Malicious DSObj, int coinCount) {
    int x = 99999;
    Members mod = new Members();
    int k = DSObj.memberlist.length;
    int a = coinCount/k;
    mod.UID = "Moderator";
    Transaction[] niketh = new Transaction[coinCount];
    int length = 0;
    for(int i=0;i<coinCount/k;i++){
      for(int j=0;j<k;j++){
        x++;
        DSObj.latestCoinID = String.valueOf(x);
        Transaction t = new Transaction();
        t.coinID = String.valueOf(x);
        t.Source = mod;
        t.Destination = DSObj.memberlist[j];
        t.coinsrc_block = null;
        niketh[i*k+j] = t;
      }
    }
    int count=0;
    Transaction[] tarray = new Transaction[DSObj.bChain.tr_count];
    TransactionBlock[] blockarray = new TransactionBlock[a];
    int numblocks = 0;
    for(int i=0;i<coinCount;i++){
      tarray[count] = niketh[i];
      count++;
      if(count==DSObj.bChain.tr_count){
        count = 0;
        TransactionBlock block = new TransactionBlock(tarray);
        DSObj.bChain.InsertBlock_Malicious(block);
        blockarray[numblocks] = block;
        numblocks = numblocks+1;
      }
    }
    int global = 0;
    for(int i=0;i<numblocks;i++){
      for(int j=0;j<DSObj.bChain.tr_count;j++){
        if(global==k){
          global = 0;
        }
        DSObj.memberlist[global].mycoins.add(new Pair<String,TransactionBlock>(blockarray[i].trarray[j].coinID,blockarray[i]));
        global++;
      }
    }
  }
}
