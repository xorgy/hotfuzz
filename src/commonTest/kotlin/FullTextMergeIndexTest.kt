import kotlin.test.Test
import kotlin.test.assertEquals

class FullTextMergeIndexTest {
    @Test
    fun integer_keys_merge() {
        val baar = FullTextMergeIndex<Int>();
        baar.insert(69, "boof");
        baar.insert(420, "foob");

        val fooo = FullTextMergeIndex<Int>();
        fooo.insert(420, "foob");
        fooo.insert(69, "boof");

        assertEquals(baar.index, fooo.index);
        assertEquals(baar.query("oof"), fooo.query("oof"));
    }

    @Test
    fun str_keys_merge() {
        val quux = FullTextMergeIndex<String>();
        quux.insert("pleased", "boof");
        quux.insert("blazed", "foob");

        val baaz = FullTextMergeIndex<String>();
        baaz.insert("blazed", "foob");
        baaz.insert("pleased", "boof");

        assertEquals(quux.index, baaz.index);
        assertEquals(quux.query("oof"), baaz.query("oof"));
    }

    @Test
    fun invert_vs_merge() {
        val baar = FullTextInvertIndex<Int>();
        baar.insert(69, "boof");
        baar.insert(420, "foob");

        val fooo = FullTextMergeIndex<Int>();
        fooo.insert(420, "foob");
        fooo.insert(69, "boof");

        assertEquals(baar.query("oof"), fooo.query("oof"));
    }

    @Test
    fun merge_vs_invert_from() {
        val baar = FullTextMergeIndex<Int>();
        baar.insert(69, "boof");
        baar.insert(420, "foob");

        val fooo = baar.toInvert();

        assertEquals(baar.query("oof"), fooo.query("oof"));
    }
}