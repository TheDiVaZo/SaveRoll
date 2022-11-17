package saveroll.permission;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
//public class PermissionPattern {
//    public static class Field {
//        protected String nameField;
//
//        protected Field(String nameField) {
//            this.nameField = nameField;
//        }
//
//        @Override
//        public boolean equals(Object o) {
//            if (this == o) return true;
//            if (!(o instanceof Field)) return false;
//            Field field = (Field) o;
//            return nameField.equals(field.nameField);
//        }
//
//        @Override
//        public int hashCode() {
//            return Objects.hash(nameField);
//        }
//    }
//
//    protected Set<Field> validFields = new HashSet<>();
//    protected Map<Field, String> valuesInFields;
//
//    protected String BEGIN_PERMISSION = "rollplus.";
//
//    protected PermissionPattern(Map<Field, String> valuesInFields) {
//        this.valuesInFields = valuesInFields;
//        validFields.add(new Field("ROLL_NAME"));
//    }
//
//    private String generatePermission(String... chunks) {
//        return BEGIN_PERMISSION + Arrays.stream(chunks).reduce("%s.%s"::formatted).get();
//    }
//
//    protected boolean hasValidField(Field field) {
//        return validFields.contains(field);
//    }
//
//    public PermissionPattern generatePattern(Map<Field, String> valuesInFields) {
//        for (Field arg : valuesInFields.keySet()) {
//            if(!hasValidField(arg)) throw new IllegalArgumentException("Недопустимые поля fields");
//        }
//        this.valuesInFields = valuesInFields;
//        return new PermissionPattern(valuesInFields);
//    }
//
//    public List<String> getValuePattern(Field... fields) {
//        return getValuePattern(Arrays.asList(fields));
//    }
//
//    public List<String> getValuePattern(List<Field> fields) {
//        ArrayList<String> values = new ArrayList<>(fields.size());
//        for (Field field : fields) {
//            if(!hasValidField(field)) throw new IllegalArgumentException("Недопустимые поля fields");
//            values.add(valuesInFields.get(field));
//        }
//        return values;
//    }
//
//    public String getPermission(List<Field> fields) {
//        return generatePermission(fields.stream().map(field -> {
//            if(!hasValidField(field)) throw new IllegalArgumentException("Недопустимые поля fields");
//            return valuesInFields.get(field);
//        }));
//    }
//}
