import kotlin.test.Test
import kotlin.test.assertEquals

class FullTextInvertIndexTest {
    @Test
    fun integer_keys_invert() {
        val baar = FullTextInvertIndex<Int>();
        baar.insert(69, "boof");
        baar.insert(420, "foob");

        val fooo = FullTextInvertIndex<Int>();
        fooo.insert(420, "foob");
        fooo.insert(69, "boof");

        assertEquals(baar.index, fooo.index);
        assertEquals(baar.query("oof"), fooo.query("oof"));
    }

    @Test
    fun str_keys_invert() {
        val quux = FullTextInvertIndex<String>();
        quux.insert("pleased", "boof");
        quux.insert("blazed", "foob");

        val baaz = FullTextInvertIndex<String>();
        baaz.insert("blazed", "foob");
        baaz.insert("pleased", "boof");

        assertEquals(quux.index, baaz.index);
        assertEquals(quux.query("oof"), baaz.query("oof"));
    }

    @Test
    fun invert_vs_merge_from() {
        val baar = FullTextInvertIndex<Int>();
        baar.insert(69, "boof");
        baar.insert(420, "foob");

        val fooo = baar.toMerge();

        assertEquals(baar.query("oof"), fooo.query("oof"));
    }


    @Test
    fun test_start_end_sensitive() {
        val baar = FullTextInvertIndex<String>();

        baar.insertBounded("blazed", "Adiaeresis");
        baar.insertBounded("crazed", "Aacute");
        baar.insertBounded("pleased", "A");

        assertEquals(baar.queryBounded("A")[0], "pleased");
        assertEquals(baar.queryBounded("Ate")[0], "crazed");
        assertEquals(baar.queryBounded("Ais")[0], "blazed");
        assertEquals(baar.queryBounded("Ad")[0], "blazed");
    }

    @Test
    fun test_multibyte() {
        val baar = FullTextInvertIndex<String>();

        baar.insertBounded("chickity", "中ity中國，這中華的雞");
        baar.insertBounded("kurosawa", "like 黒沢 I make mad films");
        baar.insertBounded("sushi", "I like the 寿司 'cause it's never touched a frying pan");

        assertEquals(baar.queryBounded("寿司")[0], "sushi");
        assertEquals(baar.queryBounded("黒沢")[0], "kurosawa");
        assertEquals(baar.queryBounded("中華的雞")[0], "chickity");
    }

    @Test
    fun test_sub_grapheme_match() {
        val baar = FullTextInvertIndex<String>();

        baar.insertBounded("제11조 ①", "제11조 ① 모든 국민은 법 앞에 평등하다. 누구든지 성별·종교 또는 사회적 신분에 의하여 정치적·경제적·사회적·문화적 생활의 모든 영역에 있어서 차별을 받지 아니한다.");
        baar.insertBounded("-e", "법률에");
        baar.insertBounded("-i", "법률이");

        assertEquals(baar.queryBounded("모든 국민은 법 앞에 평등하ᄃ")[0], "제11조 ①");
        assertEquals(baar.queryBounded("법률이")[0], "-i");
        assertEquals(baar.queryBounded("법률에")[0], "-e");
        assertEquals(baar.queryBounded("법률이")[1], "-e");
    }

}