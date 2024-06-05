package com.zznode.dhmp.data.process.sensitive.format;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * SensitiveFormatter构建器
 *
 * @author 王俊
 */
public final class SensitiveFormatterBuilder {

    /**
     * 屏蔽字符
     */
    private static final String MASK = "*";

    private final List<SensitivePrintParser> parsers = new ArrayList<>();

    /**
     * 所有内容替换为*
     *
     * @return this
     */
    public SensitiveFormatterBuilder shieldAll() {
        addParser(new FixedLengthPrintParser());
        return this;
    }

    /**
     * 替换为固定长度的*
     *
     * @return this
     */
    public SensitiveFormatterBuilder fixedLength(int width) {
        addParser(new FixedLengthPrintParser(width));
        return this;
    }


    /**
     * 从下标{@code start}开始，根据{@link FillMode} 填充{@code width} 长度的内容
     *
     * @param start    开始下标。如果是负数，从length-start个位置开始屏蔽
     * @param width    内容长度。如果是负数,则一直屏蔽到末尾
     * @param fillMode 填充模式。
     * @return this
     */
    public SensitiveFormatterBuilder shieldRange(int start, int width, FillMode fillMode) {
        addParser(new IndexedFillPrintParser(start, width, fillMode));
        return this;
    }


    /**
     * 从下标{@code start}开始，屏蔽{@code width} 长度的内容
     *
     * @param start 开始下标。如果是负数，从length-start个位置开始屏蔽
     * @param width 内容长度。如果是负数,则一直屏蔽到末尾
     * @return this
     */
    public SensitiveFormatterBuilder shieldRange(int start, int width) {
        addParser(new IndexedFillPrintParser(start, width));
        return this;
    }


    /**
     * 取消屏蔽。
     * <p>
     * 一般用于{@link #shieldAll()}之后，将下标{@code start}(包含)至{@code start+width}(不包含)之间的原始内容显示出来。
     *
     * @param start 开始下标
     * @param width 内容长度
     * @return this
     */
    public SensitiveFormatterBuilder unShield(int start, int width) {
        addParser(new IndexedFillPrintParser(start, width, true));
        return this;
    }

    /**
     * 搜索给定的字符串{@code text}的位置，从{@code text}在字符串中的起始位置开始，根据{@code position}模式屏蔽前面/后面/自身所有内容。
     *
     * <p>
     * 例子：
     * <p> 原字符串："abc@qq.com", 搜索字符串："@", position: {@link  ShieldPosition#BEFORE}, 则结果为 "***@qq.com"
     * <p> 原字符串："我是王俊, 我很帅", 搜索字符串："王俊", position: {@link ShieldPosition#SELF}, 则结果为 "我是王**, 我很帅"
     *
     * @param text     搜索字符串
     * @param position 替换内容在搜索字符的位置常量，前中后 {@link ShieldPosition}
     * @return this
     */
    public SensitiveFormatterBuilder shieldTextPosition(String text, ShieldPosition position) {
        addParser(new TextSearchPrintParser(text, position));
        return this;
    }

    /**
     * 搜索给定的字符串{@code text}的位置，从{@code text}在字符串中的起始位置开始，根据{@code position}模式，将前/后/自身所有内容替换为{@code width}长度的{@code *}。
     * <p>
     * 例子：
     * <p> 原字符串："abc@qq.com", 搜索字符串："@", position: {@link  ShieldPosition#BEFORE}, width: 4, 则结果为 "****@qq.com"
     * <p> 原字符串："我是王俊, 我很帅", 搜索字符串："王俊", position: {@link ShieldPosition#SELF}, width: 4, 则结果为 "我是****, 我很帅"
     *
     * @param text     搜索字符串
     * @param position 替换内容在搜索字符的位置常量，前中后 {@link ShieldPosition}
     * @param width    屏蔽字符长度
     * @return this
     */
    public SensitiveFormatterBuilder shieldTextPosition(String text, ShieldPosition position, int width) {
        addParser(new TextSearchPrintParser(text, position, width));
        return this;
    }

    /**
     * 根据模式字符串构建。
     * <p>
     * 建议单独使用
     *
     * @param pattern 模式字符串
     * @return this
     */
    public SensitiveFormatterBuilder appendPattern(String pattern) {
        Objects.requireNonNull(pattern, "pattern");
        parsePattern(pattern);
        return this;
    }


    void addParser(SensitivePrintParser parser) {
        this.parsers.add(parser);
    }


    public SensitiveFormatter build() {
        CompositePrinterParser parser = new CompositePrinterParser(this.parsers);
        return new SensitiveFormatter(parser);
    }


    /**
     * 解析给定的模式字符串。
     * 该模式字符串包含一系列的数字范围，这些范围可以通过'&'符号分隔，并可以选择性地通过'#'符号来表示其相反的范围。
     * 具体的范围格式为：#{起始数字,结束数字}&{起始数字,结束数字}。
     *
     * <ul>
     *     <li>
     *         用{@code {}}包裹每个部分
     *         <p>主要格式为{@code {起始数字,长度}}，表示从其实数字开始，屏蔽{长度}个字符。对应 {@link SensitiveFormatterBuilder#shieldRange(int, int)}
     *     </li>
     *     <li> {@code #}：只能在{@code {}}前面,表示{@code {}}中的内容需要反转，即将屏蔽的内容显示为原始内容。对应 {@link SensitiveFormatterBuilder#unShield(int, int)}</li>
     *     <li>
     *         {@code S}：只能在{@code {}}前面时，{@code {}}中的内容只能是 {@code [字符](B|A|S)数字}, 其中"字符"被{@code []}包裹
     *          <p>如 {@code S{[@]B2}}表示搜索"@"之前的内容替换为2个长度的屏蔽字符。 对应 {@link SensitiveFormatterBuilder#shieldTextPosition(String, ShieldPosition, int)}
     *          <p>数字可以省略。对应 {@link SensitiveFormatterBuilder#shieldTextPosition(String, ShieldPosition)}
     *          <p>B、A、S分别对应{@link ShieldPosition }中的{@link ShieldPosition#BEFORE},{@link ShieldPosition#AFTER},{@link ShieldPosition#SELF}，
     *          如果省略，则默认为{@link ShieldPosition#SELF}
     *     </li>
     *     <li>{@code F}：只能在{@code {}}前面，表示将字符串替换为固定长度的屏蔽字符
     *     <p>如 {@code F{6}}表示将字符串替换为6个长度的屏蔽字符。 对应 {@link SensitiveFormatterBuilder#fixedLength(int)}
     *     </li>
     *     <li>{@code &}：分割每个部分</li>
     * </ul>
     * <p>
     * 如果存在{@code &}分割的多个部分，则会按照从左往右顺序依次处理。
     *
     * @param pattern 模式字符串，格式为#{1,2}&{2,3}等。
     */
    private void parsePattern(String pattern) {
        // 如果模式字符串为空，则直接返回不做任何处理
        if (pattern.isEmpty()) {
            return;
        }
        // 标记当前范围是否需要反转
        boolean invert = false;
        // 内容的类型, '0'代表默认的
        char contentMode = '0';
        // 遍历模式字符串中的每一个字符
        for (int i = 0; i < pattern.length(); i++) {
            char cur = pattern.charAt(i);

            if (cur == '#') {
                contentMode = '0';
                invert = true;
                continue;
            }
            if (cur == 'S' || cur == 'F') {
                contentMode = cur;
                continue;
            }
            if (cur == '{') {
                // 下标右移一位
                i++;

                // 开始解析{}中的内容
                switch (contentMode) {
                    case 'S':
                        cur = pattern.charAt(i++);
                        StringBuilder builder = new StringBuilder();
                        if (cur == '[') {
                            while ((cur = pattern.charAt(i++)) != ']') {
                                builder.append(cur);
                            }
                        }
                        String searchText = builder.toString();
                        ShieldPosition shieldPosition = ShieldPosition.SELF;
                        cur = pattern.charAt(i++);
                        if (cur == 'A' || cur == 'B' || cur == 'S') {
                            shieldPosition = switch (cur) {
                                case 'B' -> ShieldPosition.BEFORE;
                                case 'A' -> ShieldPosition.AFTER;
                                case 'S' -> ShieldPosition.SELF;
                                default -> throw new IllegalArgumentException("invalid pattern");
                            };
                        }
                        int fillWidth = -1;
                        while ((cur = pattern.charAt(i++)) >= '0' && cur <= '9') {
                            fillWidth *= 10;
                            fillWidth += cur - '0';
                        }

                        addParser(new TextSearchPrintParser(searchText, shieldPosition, fillWidth));

                        break;
                    case 'F':

                        int w = 0;
                        while ((cur = pattern.charAt(i++)) >= '0' && cur <= '9') {
                            int digit = Character.digit(cur, 10);
                            w *= 10;
                            w += digit;
                        }
                        addParser(new FixedLengthPrintParser(w));
                        break;
                    default:

                        // 范围的起始位置
                        int start = 0;
                        // 范围的长度
                        int len = 0;

                        char firstChar = pattern.charAt(i);
                        boolean negative = false;
                        if (firstChar < '0') {
                            if (firstChar == '-') {
                                negative = true;
                            } else if (firstChar != '+') {
                                throw new NumberFormatException("cannot parse number for pattern: " + pattern + ", index: " + (i - 1));
                            }
                            i++;
                        }

                        while ((cur = pattern.charAt(i++)) >= '0' && cur <= '9') {
                            int digit = Character.digit(cur, 10);
                            assert digit >= 0;
                            start *= 10;
                            start += digit;
                        }

                        start = negative ? -start : start;
                        // 如果存在逗号，则解析长度
                        if (cur == ',') {
                            while ((cur = pattern.charAt(i++)) >= '0' && cur <= '9') {
                                int digit = Character.digit(cur, 10);
                                len *= 10;
                                len += digit;
                            }
                        }

                        addParser(new IndexedFillPrintParser(start, len, invert));
                        invert = false;
                        break;
                }

                // 解析完这部分后重置
                contentMode = '0';
            }


        }

    }

    /**
     * 格式化策略
     */
    interface SensitivePrintParser {
        /**
         * 格式化字符传
         *
         * @param str 字符串
         * @param buf 结果字符构造器
         */
        void format(String str, StringBuilder buf);
    }

    static final class CompositePrinterParser implements SensitivePrintParser {

        private final List<SensitivePrintParser> parsers;

        CompositePrinterParser(List<SensitivePrintParser> parsers) {
            this.parsers = parsers;
        }

        @Override
        public void format(String str, StringBuilder buf) {

            for (SensitivePrintParser pp : parsers) {
                pp.format(str, buf);
            }
        }
    }

    static abstract class AbstractFillPrintParser implements SensitivePrintParser {

        private final FillMode fillMode;

        AbstractFillPrintParser(FillMode fillMode) {
            this.fillMode = fillMode;
        }

        /**
         * @param buf   StringBuilder
         * @param start 开始下标
         * @param end   结束下标
         * @param str   填充的字符串
         */
        protected void fill(StringBuilder buf, int start, int end, String str) {

            switch (fillMode) {
                case INSERT -> buf.insert(start, str);
                case COVER -> buf.replace(start, end, str);
                case CUT_COVER -> buf.replace(start, buf.length(), str);
                case REPLACE -> buf.replace(start, start + str.length(), str);
            }
        }

    }

    static class IndexedFillPrintParser extends AbstractFillPrintParser implements SensitivePrintParser {


        /**
         * 开始下标。
         * <p>
         * <p>
         * 可以是负数，表示从结尾{@code -start}位置开始
         */
        private final int start;
        /**
         * 长度
         * <p>
         * 可以是负数，表示从{@link #start} 到结尾的所有字符都替换。
         */
        private final int width;

        /**
         * {@code true}: 表示下标{@code start (包含)} 到 {@code start + width (不包含)}的字符串将显示原来的值
         * <p>
         * {@code false}: 表示下标{@code start (包含)} 到 {@code start + width (不包含)}的字符串将显示*
         */
        private final boolean invert;


        IndexedFillPrintParser(int start, int width) {
            this(start, width, false);
        }

        IndexedFillPrintParser(int start, int width, boolean invert) {
            this(start, width, FillMode.REPLACE, invert);
        }

        IndexedFillPrintParser(int start, int width, FillMode fillMode) {
            this(start, width, fillMode, false);
        }

        IndexedFillPrintParser(int start, int width, FillMode fillMode, boolean invert) {
            super(fillMode);
            this.start = start;
            this.width = width;
            this.invert = invert;
        }


        @Override
        public void format(String str, StringBuilder buf) {

            int strLen = str.length();

            int startPos = start >= 0 ? start : strLen + start;

            int endPos = width >= 0 ? startPos + width : strLen;

            int count = endPos - startPos;

            String replaceStr;
            if (invert) {
                replaceStr = str.substring(startPos, endPos);
            }
            else {
                replaceStr = MASK.repeat(count);
            }
            fill(buf, startPos, endPos, replaceStr);
        }
    }

    final static class TextSearchPrintParser extends AbstractFillPrintParser implements SensitivePrintParser {


        /**
         * 匹配字符串
         */
        private final String searchText;
        /**
         * 屏蔽位置
         */
        private final ShieldPosition shieldPosition;
        /**
         * 填充宽度，如果大于0，表示用此长度的屏蔽字符替换原来的字符
         */
        private final int fillWidth;

        TextSearchPrintParser(String searchText, ShieldPosition shieldPosition) {
            this(searchText, shieldPosition, -1);
        }

        TextSearchPrintParser(String searchText, ShieldPosition shieldPosition, int fillWidth) {
            super(FillMode.COVER);
            this.searchText = searchText;
            this.shieldPosition = shieldPosition;
            this.fillWidth = fillWidth;
        }

        @Override
        public void format(String str, StringBuilder buf) {

            int searchIndex = str.indexOf(searchText);
            if (searchIndex == -1) {
                return;
            }
            int startPos;
            int endPos;
            int replaceLength;
            switch (shieldPosition) {
                case BEFORE -> {
                    startPos = 0;
                    endPos = searchIndex;
                    replaceLength = searchIndex;
                }
                case AFTER -> {
                    startPos = searchIndex + searchText.length();
                    endPos = str.length();
                    replaceLength = str.length() - searchIndex - searchText.length();
                }
                case SELF -> {
                    startPos = searchIndex;
                    endPos = searchIndex + searchText.length();
                    replaceLength = searchText.length();
                }
                default -> throw new IllegalStateException();
            }
            if (fillWidth >= 0) {
                replaceLength = fillWidth;
            }

            fill(buf, startPos, endPos, MASK.repeat(replaceLength));
        }
    }

    /**
     * 替换为固定长度的字符串
     */
    final static class FixedLengthPrintParser implements SensitivePrintParser {
        /**
         * 替换后字符的长度
         * <p>
         * 如果小于或等于{@code 0}， 则将所有字符替换为{@code *}，否则，替换为原字符串长度的{@code *}
         * <p>
         * 如：str: "1234567890", width: 3. ->  ***
         */
        private final int width;

        FixedLengthPrintParser() {
            this(0);
        }

        FixedLengthPrintParser(int width) {
            this.width = width;
        }

        @Override
        public void format(String str, StringBuilder buf) {
            int count = width > 0 ? width : str.length();
            buf.delete(0, buf.length());
            buf.append(MASK.repeat(count));
        }
    }

}
