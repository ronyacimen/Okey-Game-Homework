import java.util.ArrayList;
import java.util.Random;

public class OkeyGame {
   

    Player[] players;
    Tile[] tiles;
    int count;

    Tile lastDiscardedTile;

    int currentPlayerIndex = 0;

    public OkeyGame() {
        players = new Player[4];
        for(int i = 0; i < players.length; i++){
            players[i] = new Player("");
        }
        count = 0;
    }

    public void createTiles() {
        tiles = new Tile[112];
        int currentTile = 0;

        // two copies of each color-value combination, no jokers
        for (int i = 1; i <= 7; i++) {
            for (int j = 0; j < 4; j++) {
                tiles[currentTile++] = new Tile(i,'Y');
                tiles[currentTile++] = new Tile(i,'B');
                tiles[currentTile++] = new Tile(i,'R');
                tiles[currentTile++] = new Tile(i,'K');
            }
        }
    }

    /*
     * TODO: distributes the starting tiles to the players
     * player at index 0 gets 15 tiles and starts first
     * other players get 14 tiles
     * this method assumes the tiles are already shuffled
     */
    public void distributeTilesToPlayers() {
        for ( int ind = 0; ind < players.length; ind++ ) {
            if ( ind == 0 ) {
                for ( int i = 0; i < 15; i++ ) {
                    players[ind].addTile(tiles[i]);
                    count++;
                }
            }
            else {
                for ( int i = 15 + (ind - 1) * 14; i < 15 + ind * 14; i++ ) {
                    players[ind].addTile(tiles[i]);
                    count++;
                }
            }
        }
    }

    /*
     * TODO: get the last discarded tile for the current player
     * (this simulates picking up the tile discarded by the previous player)
     * it should return the toString method of the tile so that we can print what we picked
     */
    public String getLastDiscardedTile() {
        Player curPlayer = players[currentPlayerIndex];
        if ( lastDiscardedTile != null ) {
            curPlayer.addTile(lastDiscardedTile);
            ApplicationMain.discardedTiles.remove(lastDiscardedTile);
            return lastDiscardedTile.toString();
        }
        return null;
    }

    /*
     * TODO: get the top tile from tiles array for the current player
     * that tile is no longer in the tiles array (this simulates picking up the top tile)
     * it should return the toString method of the tile so that we can print what we picked
     */
    public String getTopTile() {
        if(tiles[0] != null){
            Tile topTile = tiles[0];
            Player curPlayer = players[currentPlayerIndex];
            
            curPlayer.addTile(topTile);
            if(topTile != null){
                for(int i = 1; i < tiles.length; i++){
                    tiles[i - 1] = tiles[i];
                }
                count++;
                tiles[tiles.length - 1 - count] = null;
                return topTile.toString();
            }
        }
        else{
            System.out.println("Draw");
            ApplicationMain.didEnd = true;
        }
        return null;
    }

    /*
     * TODO: should randomly shuffle the tiles array before game starts
     */
    public void shuffleTiles() {
        Random rnd = new Random();
        for(int i = tiles.length - 1; i > 0; i--){
            int random = rnd.nextInt(i + 1);
            Tile temp = tiles[i];
            tiles[i] = tiles[random];
            tiles[random] = temp;
        }
    }

    /*
     * TODO: check if game still continues, should return true if current player
     * finished the game, use isWinningHand() method of Player to decide
     */
    public boolean didGameFinish() {
        Player currentPlayer = players[currentPlayerIndex];

        if(currentPlayer.isWinningHand()){
            return true;
        }
        return false;
    }

    /*
     * TODO: Pick a tile for the current computer player using one of the following:
     * - picking from the tiles array using getTopTile()
     * - picking from the lastDiscardedTile using getLastDiscardedTile()
     * You should consider if the discarded tile is useful for the computer in
     * the current status. Print whether computer picks from tiles or discarded ones.
     */
    public void pickTileForComputer() {
        Player currentPlayer = players[currentPlayerIndex];
        boolean pickedATile = false;
        ArrayList<Tile> tiles = new ArrayList<>();
        int chainSize = 0;

        for (int i = 0; i < currentPlayer.numberOfTiles; i++) {
            tiles.add(currentPlayer.getTiles()[i]);
        }

        if (lastDiscardedTile != null && tiles.contains(lastDiscardedTile) == false ) {
            for( int i = 0 ; i < currentPlayer.numberOfTiles ; i++ ){
                if(lastDiscardedTile.canFormChainWith(currentPlayer.getTiles()[i])){
                    chainSize++;
                }
            }
            if(chainSize > 1 && chainSize < 4){
                getLastDiscardedTile();
                pickedATile = true;
                System.out.println(currentPlayer.getName() + " picked a tile from discarded tiles");
            }
        }


        if (pickedATile == false) {
            String pickedTile = getTopTile();
            System.out.println(currentPlayer.getName() + " picked a tile from tile stack");
            
            if (ApplicationMain.didEnd) { 
                return;
            }
        
            if (pickedTile == null) {
                System.out.println("Game has finished");
                ApplicationMain.didEnd = true;
                return;
            }
        
            System.out.println(currentPlayer.getName() + " picked a tile from tile stack");
        }
        
    }

    /*
     * TODO: Current computer player will discard the least useful tile.
     * this method should print what tile is discarded since it should be
     * known by other players. You may first discard duplicates and then
     * the single tiles and tiles that contribute to the smallest chains.
     */
    public void discardTileForComputer() {
        Player player = players[currentPlayerIndex];
        Tile[] tiles = player.getTiles();
        ArrayList<Integer> count = new ArrayList<>();
        Tile current = null;
        Tile next = null;
        for(int i=0; i< tiles.length - 1; i++){
            current = tiles[i];
            for(int j=i+1; j<tiles.length; j++){
                next = tiles[j];
                if(current.getValue() == next.getValue()){
                }
                if(current.compareTo(next)==0){
                    lastDiscardedTile= player.getAndRemoveTile(i);
                    displayDiscardInformation();
                    return;
                }
            }
        }
        int c;
        for(int i=1;i<8;i++){
            c =0;
            for(Tile tile : tiles){
                if(i == tile.getValue()){
                    c++;
                }
            }
            count.add(c);
        }
        int min=10;
        int value =0;
        for(int i=0; i<count.size(); i++){
            if(count.get(i) !=0 && count.get(i)<min){
                value = i+1;
            }
            
        }
        if(tiles.length!=0){
            int x =findValueInArray(value, tiles);
            lastDiscardedTile= player.getAndRemoveTile(x);
        }
       
        
        
    } 
    
    public int findValueInArray(int value, Tile[] tiles){
        
        for(int i=0; i<tiles.length; i++){
            if(value == tiles[i].getValue()){
                return i;
            }
        }
        return 0;
    }
    

    /*
     * TODO: discards the current player's tile at given index
     * this should set lastDiscardedTile variable and remove that tile from
     * that player's tiles
     */
    public void discardTile(int tileIndex) {
        Player player = players[currentPlayerIndex];
        lastDiscardedTile = player.getAndRemoveTile(tileIndex);
    }

    public void displayDiscardInformation() {
        if(lastDiscardedTile != null) {
            System.out.println("Last Discarded: " + lastDiscardedTile.toString());
        }
    }

    public void displayCurrentPlayersTiles() {
        players[currentPlayerIndex].displayTiles();
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

      public String getCurrentPlayerName() {
        return players[currentPlayerIndex].getName();
    }

    public void passTurnToNextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % 4;
    }

    public void setPlayerName(int index, String name) {
        if(index >= 0 && index <= 3) {
            players[index] = new Player(name);
    
        }
    }

    public void isTilesEmpty() {
       
        if ( tiles[0] == null ) {
            System.out.println("DRRAAAWWWWW");
           ApplicationMain.didEnd= true;
        }
    }

    public int getCount(){
        return count;
    }

    public void setCount(int newCount){
        count = newCount;
    }
}
