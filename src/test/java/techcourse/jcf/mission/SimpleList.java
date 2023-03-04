package techcourse.jcf.mission;

public interface SimpleList<T> {

    boolean add(T value);

    void add(int index, T value);

    T set(int index, T value);

    T get(int index);

    boolean contains(T value);

    int indexOf(T value);

    int size();

    boolean isEmpty();

    boolean remove(T value);

    T remove(int index);

    void clear();

    static <T> SimpleList<T> fromArrayToList(T[] array) {
        SimpleList<T> list = new SimpleLinkedList<>();

        for (T value : array) {
            list.add(value);

        }
        return list;
    }

    static <T extends Number> double sum(SimpleList<T> list) {
        double sum = 0;

        for (int i = 0; i < list.size(); i++) {
            T value = list.get(i);

            if (!(value instanceof Number)) {
                throw new IllegalArgumentException("숫자만 가능합니다.");
            }

            sum += ((Number) value).doubleValue();
        }
        return sum;
    }
}
