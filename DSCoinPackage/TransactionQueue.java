package DSCoinPackage;

public class TransactionQueue {

  public Transaction firstTransaction;
  public Transaction lastTransaction;
  public int numTransactions;

  public void AddTransactions (Transaction transaction) {
    if(firstTransaction==null){
      transaction.previous = null;
      transaction.next = null;
      firstTransaction = transaction;
      lastTransaction = transaction;
      numTransactions = 1;
    }
    else{
      Transaction temp = lastTransaction;
      lastTransaction.next = transaction;
      transaction.previous = temp;
      lastTransaction = transaction;
      numTransactions = numTransactions+1;
    }
  }
  
  public Transaction RemoveTransaction () throws EmptyQueueException {
    if(firstTransaction!=null){
      if(firstTransaction==lastTransaction){
        Transaction temp = firstTransaction;
        firstTransaction = null;
        lastTransaction = null;
        numTransactions = numTransactions-1;
        return temp;
      }
      else{
        Transaction temp = firstTransaction;
        Transaction temp1 = firstTransaction.next;
        temp.next = null;
        temp1.previous = null;
        firstTransaction = temp1;
        numTransactions = numTransactions-1;
        return temp;
      }
    }
    else{
      throw new EmptyQueueException();
    }
  }

  public int size() {
    return numTransactions;
  }
}
