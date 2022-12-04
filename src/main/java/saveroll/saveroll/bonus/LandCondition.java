//package saveroll.saveroll.bonus;
//
//import me.angeschossen.lands.api.integration.LandsIntegration;
//import me.angeschossen.lands.api.land.Land;
//import org.bukkit.entity.Player;
//import org.jetbrains.annotations.NotNull;
//import saveroll.saveroll.importancelevelobject.ImportanceLevelObject;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//
//public class LandCondition extends Condition {
//
//    private final ArrayList<LandBonus> landBonuses;
//    private final LandsIntegration landsIntegration;
//
//    protected static class LandBonus {
//        protected List<ImportanceLevelObject<Land>> lands;
//
//        public LandBonus(List<ImportanceLevelObject<Land>> lands) {
//            this.lands = lands;
//        }
//
//        private static List<ImportanceLevelObject<Land>> generateLand(@NotNull List<String> lands) {
//            List<ImportanceLevelObject<Land>> generatedLands = new ArrayList<>();
//            for (String land : lands) {
//                if(Objects.isNull(land)) continue;
//
//            }
//        }
//    }
//
//    protected LandCondition(LandsIntegration landsIntegration, ArrayList<LandBonus> landBonuses) {
//        super("land");
//        this.landsIntegration = landsIntegration;
//        this.landBonuses = landBonuses;
//    }
//
//    @Override
//    public int calculateRoll(Player player) {
//        return 0;
//    }
//}
