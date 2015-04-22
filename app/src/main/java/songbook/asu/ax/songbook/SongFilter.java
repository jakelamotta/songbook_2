package songbook.asu.ax.songbook;

/**
 * Created by Kristian on 2015-03-24.
 */
public interface SongFilter {
    public void filterByName(String query);
    public void filterByMelody(String query);
    public void filterByCategory(String query);
}
