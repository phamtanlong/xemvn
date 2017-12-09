package ptl.xemvn.meme;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class MemeContent {

    public static final List<MemeItem> listItems = new ArrayList<MemeItem>();

    static {
        addItem ( new MemeItem ("One-Does-Not-Simply", "http://192.168.1.109/meme/One-Does-Not-Simply.jpg"));
        addItem ( new MemeItem ("Batman-Slapping-Robin", "http://192.168.1.109/meme/Batman-Slapping-Robin.jpg"));
        addItem ( new MemeItem ("Ancient-Aliens", "http://192.168.1.109/meme/Ancient-Aliens.jpg"));
        addItem ( new MemeItem ("Futurama-Fry", "http://192.168.1.109/meme/Futurama-Fry.jpg"));
        addItem ( new MemeItem ("The-Most-Interesting-Man-In-The-World", "http://192.168.1.109/meme/The-Most-Interesting-Man-In-The-World.jpg"));
        addItem ( new MemeItem ("X-Everywhere", "http://192.168.1.109/meme/X-Everywhere.jpg"));
        addItem ( new MemeItem ("Waiting-Skeleton", "http://192.168.1.109/meme/Waiting-Skeleton.jpg"));
        addItem ( new MemeItem ("Leonardo-Dicaprio-Cheers", "http://192.168.1.109/meme/Leonardo-Dicaprio-Cheers.jpg"));
        addItem ( new MemeItem ("First-World-Problems", "http://192.168.1.109/meme/First-World-Problems.jpg"));
        addItem ( new MemeItem ("Brace-Yourselves-X-is-Coming", "http://192.168.1.109/meme/Brace-Yourselves-X-is-Coming.jpg"));
        addItem ( new MemeItem ("Bad-Luck-Brian", "http://192.168.1.109/meme/Bad-Luck-Brian.jpg"));
        addItem ( new MemeItem ("Oprah-You-Get-A", "http://192.168.1.109/meme/Oprah-You-Get-A.jpg"));
        addItem ( new MemeItem ("That-Would-Be-Great", "http://192.168.1.109/meme/That-Would-Be-Great.jpg"));
        addItem ( new MemeItem ("Y-U-No", "http://192.168.1.109/meme/Y-U-No.jpg"));
        addItem ( new MemeItem ("Boardroom-Meeting-Suggestion", "http://192.168.1.109/meme/Boardroom-Meeting-Suggestion.jpg"));
        addItem ( new MemeItem ("Creepy-Condescending-Wonka", "http://192.168.1.109/meme/Creepy-Condescending-Wonka.jpg"));
        addItem ( new MemeItem ("But-Thats-None-Of-My-Business", "http://192.168.1.109/meme/But-Thats-None-Of-My-Business.jpg"));
        addItem ( new MemeItem ("Doge", "http://192.168.1.109/meme/Doge.jpg"));
        addItem ( new MemeItem ("Captain-Picard-Facepalm", "http://192.168.1.109/meme/Captain-Picard-Facepalm.jpg"));
        addItem ( new MemeItem ("Yall-Got-Any-More-Of", "http://192.168.1.109/meme/Yall-Got-Any-More-Of.jpg"));
        addItem ( new MemeItem ("Success-Kid", "http://192.168.1.109/meme/Success-Kid.jpg"));
        addItem ( new MemeItem ("Grumpy-Cat", "http://192.168.1.109/meme/Grumpy-Cat.jpg"));
        addItem ( new MemeItem ("X-All-The-Y", "http://192.168.1.109/meme/X-All-The-Y.jpg"));
        addItem ( new MemeItem ("Third-World-Skeptical-Kid", "http://192.168.1.109/meme/Third-World-Skeptical-Kid.jpg"));
        addItem ( new MemeItem ("Matrix-Morpheus", "http://192.168.1.109/meme/Matrix-Morpheus.jpg"));
        addItem ( new MemeItem ("Black-Girl-Wat", "http://192.168.1.109/meme/Black-Girl-Wat.jpg"));
        addItem ( new MemeItem ("The-Rock-Driving", "http://192.168.1.109/meme/The-Rock-Driving.jpg"));
        addItem ( new MemeItem ("Star-Wars-Yoda", "http://192.168.1.109/meme/Star-Wars-Yoda.jpg"));
        addItem ( new MemeItem ("Picard-Wtf", "http://192.168.1.109/meme/Picard-Wtf.jpg"));
        addItem ( new MemeItem ("Philosoraptor", "http://192.168.1.109/meme/Philosoraptor.jpg"));
        addItem ( new MemeItem ("Dr-Evil-Laser", "http://192.168.1.109/meme/Dr-Evil-Laser.jpg"));
        addItem ( new MemeItem ("Disaster-Girl", "http://192.168.1.109/meme/Disaster-Girl.jpg"));
        addItem ( new MemeItem ("Face-You-Make-Robert-Downey-Jr", "http://192.168.1.109/meme/Face-You-Make-Robert-Downey-Jr.jpg"));
        addItem ( new MemeItem ("Confession-Bear", "http://192.168.1.109/meme/Confession-Bear.jpg"));
        addItem ( new MemeItem ("Evil-Toddler", "http://192.168.1.109/meme/Evil-Toddler.jpg"));
        addItem ( new MemeItem ("Finding-Neverland", "http://192.168.1.109/meme/Finding-Neverland.jpg"));
        addItem ( new MemeItem ("Grandma-Finds-The-Internet", "http://192.168.1.109/meme/Grandma-Finds-The-Internet.jpg"));
        addItem ( new MemeItem ("Am-I-The-Only-One-Around-Here", "http://192.168.1.109/meme/Am-I-The-Only-One-Around-Here.jpg"));
        addItem ( new MemeItem ("10-Guy", "http://192.168.1.109/meme/10-Guy.jpg"));
        addItem ( new MemeItem ("Too-Damn-High", "http://192.168.1.109/meme/Too-Damn-High.jpg"));
    }

    public static MemeItem getItem (String id) {
        for (MemeItem item : listItems) {
            if (item.id.equals(id))
                return item;
        }
        return null;
    }

    private static void addItem(MemeItem item) {
        listItems.add(item);
    }

    public static class MemeItem {
        public final String id;
        public final String link;

        public MemeItem(String id, String link) {
            this.id = id;
            this.link = link;
        }
    }
}
