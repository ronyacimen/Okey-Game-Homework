import java.util.ArrayList;
import java.util.Random;

public class OkeyGame {

    Player[] players;
    Tile[] tiles;

    Tile lastDiscardedTile;

    int currentPlayerIndex = 0;

    public OkeyGame() {
        players = new Player[4];
        for(int i = 0; i < players.length; i++){
            players[i] = new Player("");
        }
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
                }
            }
            else {
                for ( int i = 15 + (ind - 1) * 14; i < 15 + ind * 14; i++ ) {
                    players[ind].addTile(tiles[i]);
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
        Tile topTile = tiles[0];
        if(topTile != null){
            for(int i = 1; i < tiles.length; i++){
                tiles[i - 1] = tiles[i];
            }
            tiles[tiles.length - 1] = null;
        }
        return topTile.toString();
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
        boolean pickedFromDiscarded = false;

        if (lastDiscardedTile != null) {
            for( int i = 0 ; i < currentPlayer.numberOfTiles ; i++ ){
                if( lastDiscardedTile.canFormChainWith(currentPlayer.getTiles()[i]) && pickedFromDiscarded == false ){
                    getLastDiscardedTile();
                    pickedFromDiscarded = true;
                    System.out.println(currentPlayer.getName() + " picked a tile from discarded tiles");
                }
            }
        }
        if( pickedFromDiscarded == false ){
            getTopTile();
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
   public void discardTileForComputer() {
    Player player = players[currentPlayerIndex];
    Tile[] tiles = player.getTiles();

    if (tiles == null || tiles.length == 0) {
        System.out.println(player.getName() + " has no tiles to discard.");
        return;
    }

    int[] count = new int[tiles.length]; 
    Tile discardTile = null;
    int discardIndex = -1;

    // Önce tekrar eden taşları say
    for (int i = 0; i < tiles.length; i++) {
        if (tiles[i] == null) continue;

        for (int j = 0; j < tiles.length; j++) {
            if (i != j && tiles[i] != null && tiles[j] != null && tiles[i].getValue() == tiles[j].getValue()) {
                count[i]++;
            }
        }
    }

    for (int i = 0; i < tiles.length; i++) {
        if (tiles[i] != null && count[i] == 0) {
            discardTile = tiles[i];
            discardIndex = i;
            break;
        }
    }

    if (discardTile == null) {
        for (int i = 0; i < tiles.length; i++) {
            if (tiles[i] != null) {
                discardTile = tiles[i];
                discardIndex = i;
                break;
            }
        }
    }

    if (discardTile != null && discardIndex != -1) {
        System.out.println(player.getName() + " discarded: " + discardTile);
        lastDiscardedTile = player.getAndRemoveTile(discardIndex);
    }
}
        for(int s=0; s<tiles.length; s++){
            if(count.get(s) ==1){
              lastDiscardedTile=  player.getAndRemoveTile(s);
                return;
            }
        }
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

}
