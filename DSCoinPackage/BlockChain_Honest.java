package DSCoinPackage;

import HelperClasses.CRF;

public class BlockChain_Honest {

  public int tr_count;
  public static final String start_string = "DSCoin";
  public TransactionBlock lastBlock;

  public void InsertBlock_Honest (TransactionBlock newBlock) {
    if(lastBlock==null){
      int i = 1000000001;
      while(true){
        CRF obj = new CRF(64);
        String k = obj.Fn("DSCoin"+"#"+newBlock.trsummary+"#"+String.valueOf(i));
        boolean flag = true;
        if(!k.substring(0,4).equals("0000")){
          flag = false;
        }
        if(flag==true){
          newBlock.dgst = k;
          newBlock.nonce = String.valueOf(i);
          break;
        }
        i++;
      } 
      newBlock.previous = null;
      lastBlock = newBlock;
    }
    else{
      int i = 1000000001;
      while(true){
        CRF obj = new CRF(64);
        String k = obj.Fn(lastBlock.dgst+"#"+newBlock.trsummary+"#"+String.valueOf(i));
        boolean flag = true;
        if(!k.substring(0,4).equals("0000")){
          flag = false;
        }
        if(flag==true){
          newBlock.dgst = k;
          newBlock.nonce = String.valueOf(i);
          break;
        }
        i++;
      } 
      newBlock.previous = lastBlock;
      lastBlock = newBlock;
    }
  }
}
