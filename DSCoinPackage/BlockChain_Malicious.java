package DSCoinPackage;

import HelperClasses.CRF;

public class BlockChain_Malicious {

  public int tr_count;
  public static final String start_string = "DSCoin";
  public TransactionBlock[] lastBlocksList;
  public int size = 0;

  public static boolean checkTransactionBlock (TransactionBlock tB) {
    boolean flag = true;
    if(!tB.dgst.substring(0,4).equals("0000")){
      return false;
    }
    if(tB.previous==null){
      CRF obj = new CRF(64);
      String k = obj.Fn(start_string+"#"+tB.trsummary+"#"+tB.nonce);
      if(k.equals(tB.dgst)){
        flag = true;
      }
      else{
        flag = false;
        return flag;
      }
    }
    String s = tB.Tree.Build(tB.trarray);
    if(s.equals(tB.trsummary)){
      flag = true;
    } 
    else{
      flag = false;
      return false;
    }
    for(int i=0;i<tB.trarray.length;i++){
      if(tB.checkTransaction(tB.trarray[i])){
        flag = true;
      }
      else{
        flag = false;
        return flag;
      }
    }
    return flag;
  }

  public TransactionBlock FindLongestValidChain () {
    int max = 0;
    int index = 0;
    TransactionBlock answer = null;
    TransactionBlock[] start = new TransactionBlock[lastBlocksList.length];
    int[] count = new int[lastBlocksList.length];
    for(int i=0;i<lastBlocksList.length;i++){
      int l = 0;
      TransactionBlock temp = lastBlocksList[i];
      while(temp!=null){
        if(checkTransactionBlock(temp)){
          if(l==0){
            start[i] = temp;
          }
          l++;
        }
        else{
          l = 0;
        }
        temp = temp.previous;
        count[i] = l;
      }
    }
    for(int i=0;i<lastBlocksList.length;i++){
      if(count[i]>max){
        max = count[i];
        answer = start[i];
      }
    }
    return answer;
  }

  public void InsertBlock_Malicious (TransactionBlock newBlock) {
    TransactionBlock insertafter = FindLongestValidChain();
    if(lastBlocksList[0]==null){
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
      lastBlocksList[0] = newBlock;
      size++;
    }
    else{
      int i = 1000000001;
      while(true){
        CRF obj = new CRF(64);
        String k = obj.Fn(insertafter.dgst+"#"+newBlock.trsummary+"#"+String.valueOf(i));
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
      boolean check = false;
      int k = 0;
      for(int j=0;j<lastBlocksList.length;j++){
        if(lastBlocksList[j]==insertafter){
          check = true;
          k = j;
          break;
        }
      }
      if(check){
        newBlock.previous = lastBlocksList[k];
        lastBlocksList[k] = newBlock;
      }
      else{
        newBlock.previous = insertafter;
        lastBlocksList[size] = newBlock;
        size++;
      }
    }
    newBlock.previous = insertafter;
  }
}
