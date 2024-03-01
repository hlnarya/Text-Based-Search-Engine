
import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ArrayList;


public class HashedDictionary<K, V> implements DictionaryInterface<K, V>,
		Serializable {
	
	private static final long serialVersionUID = 1L;
	private TableEntry<K, V>[] hashTable;
	private TableEntry<K, V>[] temp;
	private int numberOfEntries;
	private int locationUsed;
	private int numberOfCollision;
	private static int DEFAULT_SIZE = 2477;
	private static final double MAX_LOAD_FACTOR = 0.5;

	public HashedDictionary() {
		this(DEFAULT_SIZE);
	}

	public HashedDictionary(int tableSize) {
		
		int primeSize = getNextPrime(tableSize);
		hashTable = new TableEntry[primeSize];
		numberOfEntries = 0;
		locationUsed = 0;
		numberOfCollision = 0;
	}

	private class TableEntry<S, T> implements Serializable {
		private S entryKey;
		private T entryValue;
		private boolean inTable;
		
		private TableEntry(S key, T value){
			entryKey = key;
			entryValue = value;
			inTable = true;
		}
		
		public S getEntryKey() {
			return entryKey;
		}
		public T getEntryValue() {
			return entryValue;
		}
		public void setEntryValue(T entryValue) {
			this.entryValue = entryValue;
		}

		
		public boolean isIn(){
			return inTable == true;
		}
		public boolean isRemoved(){
			return inTable == false;
		}
		public void setInTable(){
			this.inTable = true;
		}
		public void setToRemoved(){
			this.inTable = false;
		}
		
	}
	
	private class KeyIterator implements Iterator<K>{

		private int currentIndex;
		private int numberLeft;
		
		private KeyIterator(){
			currentIndex = 0;
			numberLeft = numberOfEntries;
		}
		
		@Override
		public boolean hasNext() {
			return numberLeft > 0;
		}

		@Override
		public K next() {
			K result = null;
			if(hasNext()){
				while((hashTable[currentIndex] == null) || hashTable[currentIndex].isRemoved())
					currentIndex++;
				result = hashTable[currentIndex].getEntryKey();
				numberLeft--;
				currentIndex++;
			}//end if
			else
				throw new NoSuchElementException();
			return result;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}
	
	private class ValueIterator implements Iterator<V>{
		
		private int currentIndex;
		private int numberLeft;
		
		private ValueIterator(){
			currentIndex = 0;
			numberLeft = numberOfEntries;
		}
		
		@Override
		public boolean hasNext() {
			return numberLeft > 0;
		}

		@Override
		public V next() {
			V result = null;
			if(hasNext()){
			while((hashTable[currentIndex] == null) || hashTable[currentIndex].isRemoved())
				currentIndex++;
			result = hashTable[currentIndex].getEntryValue();
			numberLeft--;
			currentIndex++;
			}
			else
				throw new NoSuchElementException();
			return result;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}
	private int getNextPrime(int size){
		if(size <= 0)
			throw new RuntimeException();
		if(size % 2 == 0){
			size++;
		}
		while(!isPrime(size)){
			size = size + 2;
		}
		return size;
	}
	
	private boolean isPrime(int num){
		for(int i = 2; i <= Math.sqrt(num); i++){
			if(num % i == 0)
				return false;
		}
		return true;
	}

	
	private int getHashIndex(K key){
		
		int hashIndex =(int)(hashCodeSSFPAF(key) % hashTable.length);
		
		if(hashIndex < 0)
			hashIndex = hashIndex + hashTable.length;
		return hashIndex;
	}
	
	
	 //Simple Summation Function (SSF)
		
		 	private int hashCodeSSFPAF(K key) {
	        
			int sum = 0 ;
	        for (int i = 0; i < key.toString().length(); i++) 
	        {
	        	 sum = sum + key.toString().toLowerCase().charAt(i) - 96;
	        	 
	        }
	       
	        return sum;
	    }
		 	
		
	
	//Polynomial Accumulation Function (PAF)
		 /*
	private int hashCodeSSFPAF(K key) {
        long sumtemp = 0;
		int sum = 0 ;
        for (int i = 0; i < key.toString().length(); i++) 
        {
        	 sumtemp =  (long) ((sumtemp + (( key.toString().toLowerCase().charAt(i) - 96)*Math.pow(31,key.toString().length()-i-1)))%hashTable.length);
        	 
        }
        sum=(int)sumtemp;
        return sum;
    }
  
	 */
   
//Linear Probing (LP)
		 	/*	
	private int locate(int index, K key){
		boolean found = false;
		
		while(!found && (hashTable[index] != null)){
			
			if(hashTable[index].isIn() && key.equals(hashTable[index].getEntryKey()))
				found = true;
			else 
				index = (index + 1) % hashTable.length;
		}
		int result = -1;
		if(found)
			result = index;
		return result;
	}
	
	*/ 
	
	//Double Hashing (DH)
	 	private int locate(int index, K key){
			boolean found = false;
			int j=1;
			int d2k=31-(index%31);
			long h2k=0;
			long ind=index;
			while(!found && (hashTable[index] != null)){
				
				if(hashTable[index].isIn() && key.equals(hashTable[index].getEntryKey()))
					found = true;
				else 
				{
					h2k=j*d2k;
					ind = (ind + h2k)% hashTable.length;
					index=(int)ind ;
				}
					
				j++;
			}
			int result = -1;
			if(found)
				result = index;
			return result;
		}
		
	
	@Override
	public V add(K key, V value) {
		if ((key == null) || (value == null))
			throw new IllegalArgumentException();
			else
			{
				V oldValue;
				int index = getHashIndex(key);
				index = probe(index, key);
				
				assert (index >= 0) && (index < hashTable.length);
				if((hashTable[index] == null) || hashTable[index].isRemoved()){
					hashTable[index] = new TableEntry<K,V>(key, value);
					numberOfEntries++;
					locationUsed++;
					oldValue=null;
					//System.out.println("index: "+index+" key: "+key+" value: "+value);
				}else
				{
					oldValue = hashTable[index].getEntryValue();
					hashTable[index].setEntryValue(value);
					
					
				}
				if(isFull())
					enlargeHashTable();
				
				return oldValue;
			}
		
		
	}
	
	private void enlargeHashTable()
	{
		System.out.println("\n***Enlarging Started***\n");
		
		
	TableEntry<K, V>[] oldTable = hashTable;
	int oldSize = hashTable.length;
	DEFAULT_SIZE = getNextPrime(oldSize + oldSize);
	System.out.println("\n***New size: "+DEFAULT_SIZE+"***\n");
	
	@SuppressWarnings("unchecked")
	TableEntry<K, V>[] temp = (TableEntry<K, V>[])new TableEntry[DEFAULT_SIZE];
	hashTable = temp;
	numberOfEntries = 0; 
	
	for (int index = 0; index < oldSize; index++)
	{
	if ( (oldTable[index] != null) && oldTable[index].isIn() )
	add(oldTable[index].getEntryKey(), oldTable[index].getEntryValue());
	} 
	} 
	
	
	//Linear Probing (LP)
	/*
	private int probe(int index, K key){
		boolean found = false;
		boolean collision = false;
		int removedStateIndex = -1;
		while(found ==false && (hashTable[index] != null)){
			if(hashTable[index].isIn()){
				if(key.equals(hashTable[index].getEntryKey()))
				{
					
					found = true;
				}
					
				else
				{
					collision = true;
					index = (index + 1) % hashTable.length;
				}
					
			}
			else
			{
				if(removedStateIndex == -1)
					removedStateIndex = index;
				index = (index + 1) % hashTable.length;
			}
		}//end while
		if(collision)
			numberOfCollision++;
		if(found || (removedStateIndex == -1))
		{
			//System.out.println("index is:" + index);
			return index;
			
		}
			
		else
		{
			//System.out.println("index is:" + removedStateIndex);
			return removedStateIndex;
		}
	}
*/
	//Double Hashing (DH)
	private int probe(int index, K key){
		boolean found = false;
		boolean collision = false;
		int removedStateIndex = -1;
		int j=1;
		int d2k=31-(index%31);
		long h2k=0;
		long ind=index;
		while(found ==false && (hashTable[index] != null)){
			if(hashTable[index].isIn()){
				if(key.equals(hashTable[index].getEntryKey()))
				{
					
					found = true;
				}
					
				else
				{
					collision = true;
					h2k=j*d2k;
					ind = (ind + h2k)% hashTable.length;
					index=(int)ind ;
					
					
				}
					
			}
			else
			{
				if(removedStateIndex == -1)
					removedStateIndex = index;
				h2k=j*d2k;
				ind = (ind + h2k)% hashTable.length;
				index=(int)ind ;
				
			}
			j++;
		}//end while
		if(collision)
			numberOfCollision++;
		if(found || (removedStateIndex == -1))
		{
			//System.out.println("index is:" + index);
			return index;
			
		}
			
		else
		{
			//System.out.println("index is:" + removedStateIndex);
			return removedStateIndex;
		}
			
	}

	@Override
	public V remove(K key) {
		V removedValue = null;
		int index = getHashIndex(key);
		index = locate(index, key);
		if(index != -1){
			removedValue = hashTable[index].getEntryValue();
			hashTable[index].setToRemoved();
			numberOfEntries--;
		}
		return removedValue;
	}

	@Override
	public V getValue(K key) {
		V result = null;
		int index = getHashIndex(key);
		index = locate(index, key);
		
		if(index != -1)
			result = hashTable[index].getEntryValue();
		return result;
	}

	@Override
	public boolean contains(K key) {
		boolean result = false;
		int index = getHashIndex(key);
		index = locate(index, key);
		if(index != -1)
			result = true;
		return result;
	}

	@Override
	public Iterator<K> getKeyIterator() {
		return new KeyIterator();
	}

	@Override
	public Iterator<V> getValueIterator() {
		return new ValueIterator();
	}

	@Override
	public boolean isEmpty() {
		return numberOfEntries == 0;
	}

	public boolean isFull() {
		//System.out.println("here:"+(double)numberOfEntries/DEFAULT_SIZE);
		return ((double)numberOfEntries/DEFAULT_SIZE)>MAX_LOAD_FACTOR;
	}

	@Override
	public int getSize() {
		return numberOfEntries;
	}

	@Override
	public int getCollisionCount() {
		return numberOfCollision;
	}
	
	@Override
	public void clear() {
		numberOfEntries = 0;
		locationUsed = 0;
	}
}
