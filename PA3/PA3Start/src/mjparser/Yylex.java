/*
  mj.lex
    Tokens/Symbols for MeggyJava language.
    NO dollars, but underscores anywhere in identifiers
    (to avoid problems later when generating AVR labels from id-s)
    8 colors (not all the CPP colors)
    The values for the colors, buttons, and tones were found in
    MeggyJrSimple.h and MeggyJrSimple.cpp.
    Ignore single line comments: // until eol
    ALSO: ignore C style comments, see http://ostermiller.org/findcomment.html
    Wim Bohm and Michelle Strout, 6/2011
*/
package mjparser;
import java_cup.runtime.Symbol;


public class Yylex implements java_cup.runtime.Scanner {
	private final int YY_BUFFER_SIZE = 512;
	private final int YY_F = -1;
	private final int YY_NO_STATE = -1;
	private final int YY_NOT_ACCEPT = 0;
	private final int YY_START = 1;
	private final int YY_END = 2;
	private final int YY_NO_ANCHOR = 4;
	private final int YY_BOL = 128;
	private final int YY_EOF = 129;
	private java.io.BufferedReader yy_reader;
	private int yy_buffer_index;
	private int yy_buffer_read;
	private int yy_buffer_start;
	private int yy_buffer_end;
	private char yy_buffer[];
	private int yychar;
	private int yyline;
	private boolean yy_at_bol;
	private int yy_lexical_state;

	public Yylex (java.io.Reader reader) {
		this ();
		if (null == reader) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(reader);
	}

	public Yylex (java.io.InputStream instream) {
		this ();
		if (null == instream) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(instream));
	}

	private Yylex () {
		yy_buffer = new char[YY_BUFFER_SIZE];
		yy_buffer_read = 0;
		yy_buffer_index = 0;
		yy_buffer_start = 0;
		yy_buffer_end = 0;
		yychar = 0;
		yyline = 0;
		yy_at_bol = true;
		yy_lexical_state = YYINITIAL;
	}

	private boolean yy_eof_done = false;
	private final int YYINITIAL = 0;
	private final int yy_state_dtrans[] = {
		0
	};
	private void yybegin (int state) {
		yy_lexical_state = state;
	}
	private int yy_advance ()
		throws java.io.IOException {
		int next_read;
		int i;
		int j;

		if (yy_buffer_index < yy_buffer_read) {
			return yy_buffer[yy_buffer_index++];
		}

		if (0 != yy_buffer_start) {
			i = yy_buffer_start;
			j = 0;
			while (i < yy_buffer_read) {
				yy_buffer[j] = yy_buffer[i];
				++i;
				++j;
			}
			yy_buffer_end = yy_buffer_end - yy_buffer_start;
			yy_buffer_start = 0;
			yy_buffer_read = j;
			yy_buffer_index = j;
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}

		while (yy_buffer_index >= yy_buffer_read) {
			if (yy_buffer_index >= yy_buffer.length) {
				yy_buffer = yy_double(yy_buffer);
			}
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}
		return yy_buffer[yy_buffer_index++];
	}
	private void yy_move_end () {
		if (yy_buffer_end > yy_buffer_start &&
		    '\n' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
		if (yy_buffer_end > yy_buffer_start &&
		    '\r' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
	}
	private boolean yy_last_was_cr=false;
	private void yy_mark_start () {
		int i;
		for (i = yy_buffer_start; i < yy_buffer_index; ++i) {
			if ('\n' == yy_buffer[i] && !yy_last_was_cr) {
				++yyline;
			}
			if ('\r' == yy_buffer[i]) {
				++yyline;
				yy_last_was_cr=true;
			} else yy_last_was_cr=false;
		}
		yychar = yychar
			+ yy_buffer_index - yy_buffer_start;
		yy_buffer_start = yy_buffer_index;
	}
	private void yy_mark_end () {
		yy_buffer_end = yy_buffer_index;
	}
	private void yy_to_mark () {
		yy_buffer_index = yy_buffer_end;
		yy_at_bol = (yy_buffer_end > yy_buffer_start) &&
		            ('\r' == yy_buffer[yy_buffer_end-1] ||
		             '\n' == yy_buffer[yy_buffer_end-1] ||
		             2028/*LS*/ == yy_buffer[yy_buffer_end-1] ||
		             2029/*PS*/ == yy_buffer[yy_buffer_end-1]);
	}
	private java.lang.String yytext () {
		return (new java.lang.String(yy_buffer,
			yy_buffer_start,
			yy_buffer_end - yy_buffer_start));
	}
	private int yylength () {
		return yy_buffer_end - yy_buffer_start;
	}
	private char[] yy_double (char buf[]) {
		int i;
		char newbuf[];
		newbuf = new char[2*buf.length];
		for (i = 0; i < buf.length; ++i) {
			newbuf[i] = buf[i];
		}
		return newbuf;
	}
	private final int YY_E_INTERNAL = 0;
	private final int YY_E_MATCH = 1;
	private java.lang.String yy_error_string[] = {
		"Error: Internal error.\n",
		"Error: Unmatched input.\n"
	};
	private void yy_error (int code,boolean fatal) {
		java.lang.System.out.print(yy_error_string[code]);
		java.lang.System.out.flush();
		if (fatal) {
			throw new Error("Fatal Error.\n");
		}
	}
	private int[][] unpackFromString(int size1, int size2, String st) {
		int colonIndex = -1;
		String lengthString;
		int sequenceLength = 0;
		int sequenceInteger = 0;

		int commaIndex;
		String workString;

		int res[][] = new int[size1][size2];
		for (int i= 0; i < size1; i++) {
			for (int j= 0; j < size2; j++) {
				if (sequenceLength != 0) {
					res[i][j] = sequenceInteger;
					sequenceLength--;
					continue;
				}
				commaIndex = st.indexOf(',');
				workString = (commaIndex==-1) ? st :
					st.substring(0, commaIndex);
				st = st.substring(commaIndex+1);
				colonIndex = workString.indexOf(':');
				if (colonIndex == -1) {
					res[i][j]=Integer.parseInt(workString);
					continue;
				}
				lengthString =
					workString.substring(colonIndex+1);
				sequenceLength=Integer.parseInt(lengthString);
				workString=workString.substring(0,colonIndex);
				sequenceInteger=Integer.parseInt(workString);
				res[i][j] = sequenceInteger;
				sequenceLength--;
			}
		}
		return res;
	}
	private int yy_acpt[] = {
		/* 0 */ YY_NOT_ACCEPT,
		/* 1 */ YY_NO_ANCHOR,
		/* 2 */ YY_NO_ANCHOR,
		/* 3 */ YY_NO_ANCHOR,
		/* 4 */ YY_NO_ANCHOR,
		/* 5 */ YY_NO_ANCHOR,
		/* 6 */ YY_NO_ANCHOR,
		/* 7 */ YY_NO_ANCHOR,
		/* 8 */ YY_NO_ANCHOR,
		/* 9 */ YY_NO_ANCHOR,
		/* 10 */ YY_NO_ANCHOR,
		/* 11 */ YY_NO_ANCHOR,
		/* 12 */ YY_NO_ANCHOR,
		/* 13 */ YY_NO_ANCHOR,
		/* 14 */ YY_NO_ANCHOR,
		/* 15 */ YY_NO_ANCHOR,
		/* 16 */ YY_NO_ANCHOR,
		/* 17 */ YY_NO_ANCHOR,
		/* 18 */ YY_NO_ANCHOR,
		/* 19 */ YY_NO_ANCHOR,
		/* 20 */ YY_NO_ANCHOR,
		/* 21 */ YY_NO_ANCHOR,
		/* 22 */ YY_NO_ANCHOR,
		/* 23 */ YY_NO_ANCHOR,
		/* 24 */ YY_NO_ANCHOR,
		/* 25 */ YY_NO_ANCHOR,
		/* 26 */ YY_NO_ANCHOR,
		/* 27 */ YY_NO_ANCHOR,
		/* 28 */ YY_NO_ANCHOR,
		/* 29 */ YY_NO_ANCHOR,
		/* 30 */ YY_NO_ANCHOR,
		/* 31 */ YY_NO_ANCHOR,
		/* 32 */ YY_NO_ANCHOR,
		/* 33 */ YY_NO_ANCHOR,
		/* 34 */ YY_NO_ANCHOR,
		/* 35 */ YY_NO_ANCHOR,
		/* 36 */ YY_NO_ANCHOR,
		/* 37 */ YY_NO_ANCHOR,
		/* 38 */ YY_NO_ANCHOR,
		/* 39 */ YY_NO_ANCHOR,
		/* 40 */ YY_NO_ANCHOR,
		/* 41 */ YY_NO_ANCHOR,
		/* 42 */ YY_NO_ANCHOR,
		/* 43 */ YY_NO_ANCHOR,
		/* 44 */ YY_NO_ANCHOR,
		/* 45 */ YY_NO_ANCHOR,
		/* 46 */ YY_NO_ANCHOR,
		/* 47 */ YY_NO_ANCHOR,
		/* 48 */ YY_NO_ANCHOR,
		/* 49 */ YY_NO_ANCHOR,
		/* 50 */ YY_NO_ANCHOR,
		/* 51 */ YY_NO_ANCHOR,
		/* 52 */ YY_NO_ANCHOR,
		/* 53 */ YY_NO_ANCHOR,
		/* 54 */ YY_NO_ANCHOR,
		/* 55 */ YY_NO_ANCHOR,
		/* 56 */ YY_NO_ANCHOR,
		/* 57 */ YY_NO_ANCHOR,
		/* 58 */ YY_NO_ANCHOR,
		/* 59 */ YY_NO_ANCHOR,
		/* 60 */ YY_NO_ANCHOR,
		/* 61 */ YY_NO_ANCHOR,
		/* 62 */ YY_NO_ANCHOR,
		/* 63 */ YY_NO_ANCHOR,
		/* 64 */ YY_NO_ANCHOR,
		/* 65 */ YY_NO_ANCHOR,
		/* 66 */ YY_NO_ANCHOR,
		/* 67 */ YY_NO_ANCHOR,
		/* 68 */ YY_NO_ANCHOR,
		/* 69 */ YY_NO_ANCHOR,
		/* 70 */ YY_NO_ANCHOR,
		/* 71 */ YY_NO_ANCHOR,
		/* 72 */ YY_NO_ANCHOR,
		/* 73 */ YY_NO_ANCHOR,
		/* 74 */ YY_NO_ANCHOR,
		/* 75 */ YY_NO_ANCHOR,
		/* 76 */ YY_NO_ANCHOR,
		/* 77 */ YY_NO_ANCHOR,
		/* 78 */ YY_NO_ANCHOR,
		/* 79 */ YY_NO_ANCHOR,
		/* 80 */ YY_NO_ANCHOR,
		/* 81 */ YY_NO_ANCHOR,
		/* 82 */ YY_NOT_ACCEPT,
		/* 83 */ YY_NO_ANCHOR,
		/* 84 */ YY_NO_ANCHOR,
		/* 85 */ YY_NO_ANCHOR,
		/* 86 */ YY_NOT_ACCEPT,
		/* 87 */ YY_NO_ANCHOR,
		/* 88 */ YY_NO_ANCHOR,
		/* 89 */ YY_NOT_ACCEPT,
		/* 90 */ YY_NO_ANCHOR,
		/* 91 */ YY_NOT_ACCEPT,
		/* 92 */ YY_NO_ANCHOR,
		/* 93 */ YY_NOT_ACCEPT,
		/* 94 */ YY_NO_ANCHOR,
		/* 95 */ YY_NOT_ACCEPT,
		/* 96 */ YY_NO_ANCHOR,
		/* 97 */ YY_NOT_ACCEPT,
		/* 98 */ YY_NO_ANCHOR,
		/* 99 */ YY_NOT_ACCEPT,
		/* 100 */ YY_NO_ANCHOR,
		/* 101 */ YY_NOT_ACCEPT,
		/* 102 */ YY_NO_ANCHOR,
		/* 103 */ YY_NOT_ACCEPT,
		/* 104 */ YY_NO_ANCHOR,
		/* 105 */ YY_NOT_ACCEPT,
		/* 106 */ YY_NO_ANCHOR,
		/* 107 */ YY_NOT_ACCEPT,
		/* 108 */ YY_NO_ANCHOR,
		/* 109 */ YY_NOT_ACCEPT,
		/* 110 */ YY_NO_ANCHOR,
		/* 111 */ YY_NOT_ACCEPT,
		/* 112 */ YY_NO_ANCHOR,
		/* 113 */ YY_NOT_ACCEPT,
		/* 114 */ YY_NO_ANCHOR,
		/* 115 */ YY_NOT_ACCEPT,
		/* 116 */ YY_NO_ANCHOR,
		/* 117 */ YY_NOT_ACCEPT,
		/* 118 */ YY_NO_ANCHOR,
		/* 119 */ YY_NOT_ACCEPT,
		/* 120 */ YY_NO_ANCHOR,
		/* 121 */ YY_NOT_ACCEPT,
		/* 122 */ YY_NO_ANCHOR,
		/* 123 */ YY_NOT_ACCEPT,
		/* 124 */ YY_NO_ANCHOR,
		/* 125 */ YY_NOT_ACCEPT,
		/* 126 */ YY_NO_ANCHOR,
		/* 127 */ YY_NOT_ACCEPT,
		/* 128 */ YY_NO_ANCHOR,
		/* 129 */ YY_NOT_ACCEPT,
		/* 130 */ YY_NOT_ACCEPT,
		/* 131 */ YY_NOT_ACCEPT,
		/* 132 */ YY_NOT_ACCEPT,
		/* 133 */ YY_NOT_ACCEPT,
		/* 134 */ YY_NOT_ACCEPT,
		/* 135 */ YY_NOT_ACCEPT,
		/* 136 */ YY_NOT_ACCEPT,
		/* 137 */ YY_NOT_ACCEPT,
		/* 138 */ YY_NOT_ACCEPT,
		/* 139 */ YY_NOT_ACCEPT,
		/* 140 */ YY_NOT_ACCEPT,
		/* 141 */ YY_NOT_ACCEPT,
		/* 142 */ YY_NOT_ACCEPT,
		/* 143 */ YY_NOT_ACCEPT,
		/* 144 */ YY_NOT_ACCEPT,
		/* 145 */ YY_NOT_ACCEPT,
		/* 146 */ YY_NOT_ACCEPT,
		/* 147 */ YY_NOT_ACCEPT,
		/* 148 */ YY_NOT_ACCEPT,
		/* 149 */ YY_NOT_ACCEPT,
		/* 150 */ YY_NOT_ACCEPT,
		/* 151 */ YY_NOT_ACCEPT,
		/* 152 */ YY_NOT_ACCEPT,
		/* 153 */ YY_NOT_ACCEPT,
		/* 154 */ YY_NOT_ACCEPT,
		/* 155 */ YY_NOT_ACCEPT,
		/* 156 */ YY_NOT_ACCEPT,
		/* 157 */ YY_NOT_ACCEPT,
		/* 158 */ YY_NOT_ACCEPT,
		/* 159 */ YY_NOT_ACCEPT,
		/* 160 */ YY_NOT_ACCEPT,
		/* 161 */ YY_NOT_ACCEPT,
		/* 162 */ YY_NOT_ACCEPT,
		/* 163 */ YY_NOT_ACCEPT,
		/* 164 */ YY_NOT_ACCEPT,
		/* 165 */ YY_NOT_ACCEPT,
		/* 166 */ YY_NOT_ACCEPT,
		/* 167 */ YY_NOT_ACCEPT,
		/* 168 */ YY_NOT_ACCEPT,
		/* 169 */ YY_NOT_ACCEPT,
		/* 170 */ YY_NOT_ACCEPT,
		/* 171 */ YY_NOT_ACCEPT,
		/* 172 */ YY_NOT_ACCEPT,
		/* 173 */ YY_NOT_ACCEPT,
		/* 174 */ YY_NOT_ACCEPT,
		/* 175 */ YY_NOT_ACCEPT,
		/* 176 */ YY_NOT_ACCEPT,
		/* 177 */ YY_NOT_ACCEPT,
		/* 178 */ YY_NOT_ACCEPT,
		/* 179 */ YY_NOT_ACCEPT,
		/* 180 */ YY_NOT_ACCEPT,
		/* 181 */ YY_NOT_ACCEPT,
		/* 182 */ YY_NOT_ACCEPT,
		/* 183 */ YY_NOT_ACCEPT,
		/* 184 */ YY_NOT_ACCEPT,
		/* 185 */ YY_NOT_ACCEPT,
		/* 186 */ YY_NOT_ACCEPT,
		/* 187 */ YY_NOT_ACCEPT,
		/* 188 */ YY_NOT_ACCEPT,
		/* 189 */ YY_NOT_ACCEPT,
		/* 190 */ YY_NOT_ACCEPT,
		/* 191 */ YY_NOT_ACCEPT,
		/* 192 */ YY_NOT_ACCEPT,
		/* 193 */ YY_NOT_ACCEPT,
		/* 194 */ YY_NOT_ACCEPT,
		/* 195 */ YY_NOT_ACCEPT,
		/* 196 */ YY_NOT_ACCEPT,
		/* 197 */ YY_NOT_ACCEPT,
		/* 198 */ YY_NOT_ACCEPT,
		/* 199 */ YY_NOT_ACCEPT,
		/* 200 */ YY_NO_ANCHOR,
		/* 201 */ YY_NOT_ACCEPT,
		/* 202 */ YY_NOT_ACCEPT,
		/* 203 */ YY_NOT_ACCEPT,
		/* 204 */ YY_NOT_ACCEPT,
		/* 205 */ YY_NOT_ACCEPT,
		/* 206 */ YY_NOT_ACCEPT,
		/* 207 */ YY_NOT_ACCEPT,
		/* 208 */ YY_NOT_ACCEPT,
		/* 209 */ YY_NOT_ACCEPT,
		/* 210 */ YY_NOT_ACCEPT,
		/* 211 */ YY_NOT_ACCEPT,
		/* 212 */ YY_NOT_ACCEPT,
		/* 213 */ YY_NOT_ACCEPT,
		/* 214 */ YY_NOT_ACCEPT,
		/* 215 */ YY_NOT_ACCEPT,
		/* 216 */ YY_NOT_ACCEPT,
		/* 217 */ YY_NOT_ACCEPT,
		/* 218 */ YY_NO_ANCHOR,
		/* 219 */ YY_NOT_ACCEPT,
		/* 220 */ YY_NOT_ACCEPT,
		/* 221 */ YY_NOT_ACCEPT,
		/* 222 */ YY_NOT_ACCEPT,
		/* 223 */ YY_NOT_ACCEPT,
		/* 224 */ YY_NOT_ACCEPT,
		/* 225 */ YY_NO_ANCHOR,
		/* 226 */ YY_NOT_ACCEPT,
		/* 227 */ YY_NO_ANCHOR,
		/* 228 */ YY_NO_ANCHOR,
		/* 229 */ YY_NO_ANCHOR,
		/* 230 */ YY_NO_ANCHOR,
		/* 231 */ YY_NO_ANCHOR,
		/* 232 */ YY_NO_ANCHOR,
		/* 233 */ YY_NO_ANCHOR,
		/* 234 */ YY_NO_ANCHOR,
		/* 235 */ YY_NO_ANCHOR,
		/* 236 */ YY_NO_ANCHOR,
		/* 237 */ YY_NO_ANCHOR,
		/* 238 */ YY_NO_ANCHOR,
		/* 239 */ YY_NO_ANCHOR,
		/* 240 */ YY_NO_ANCHOR,
		/* 241 */ YY_NO_ANCHOR,
		/* 242 */ YY_NO_ANCHOR,
		/* 243 */ YY_NO_ANCHOR,
		/* 244 */ YY_NO_ANCHOR,
		/* 245 */ YY_NO_ANCHOR,
		/* 246 */ YY_NO_ANCHOR,
		/* 247 */ YY_NO_ANCHOR,
		/* 248 */ YY_NO_ANCHOR,
		/* 249 */ YY_NO_ANCHOR,
		/* 250 */ YY_NO_ANCHOR,
		/* 251 */ YY_NO_ANCHOR,
		/* 252 */ YY_NO_ANCHOR,
		/* 253 */ YY_NO_ANCHOR,
		/* 254 */ YY_NO_ANCHOR,
		/* 255 */ YY_NO_ANCHOR,
		/* 256 */ YY_NO_ANCHOR,
		/* 257 */ YY_NO_ANCHOR,
		/* 258 */ YY_NO_ANCHOR,
		/* 259 */ YY_NO_ANCHOR,
		/* 260 */ YY_NO_ANCHOR,
		/* 261 */ YY_NO_ANCHOR,
		/* 262 */ YY_NOT_ACCEPT,
		/* 263 */ YY_NOT_ACCEPT,
		/* 264 */ YY_NO_ANCHOR,
		/* 265 */ YY_NO_ANCHOR,
		/* 266 */ YY_NO_ANCHOR,
		/* 267 */ YY_NO_ANCHOR,
		/* 268 */ YY_NO_ANCHOR,
		/* 269 */ YY_NO_ANCHOR,
		/* 270 */ YY_NO_ANCHOR,
		/* 271 */ YY_NO_ANCHOR,
		/* 272 */ YY_NO_ANCHOR,
		/* 273 */ YY_NO_ANCHOR,
		/* 274 */ YY_NO_ANCHOR,
		/* 275 */ YY_NO_ANCHOR,
		/* 276 */ YY_NO_ANCHOR,
		/* 277 */ YY_NO_ANCHOR,
		/* 278 */ YY_NO_ANCHOR,
		/* 279 */ YY_NO_ANCHOR,
		/* 280 */ YY_NO_ANCHOR,
		/* 281 */ YY_NO_ANCHOR,
		/* 282 */ YY_NO_ANCHOR,
		/* 283 */ YY_NO_ANCHOR,
		/* 284 */ YY_NO_ANCHOR,
		/* 285 */ YY_NOT_ACCEPT,
		/* 286 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"67:9,70,68,67,70,69,67:18,70,62,67:4,60,67,53,54,49,48,51,50,24,66,63:3,46," +
"63:6,67,52,61,59,67:3,27,32,33,30,29,47,38,45,43,64,35,28,25,37,36,26,64,34" +
",19,44,41,42,40,64,39,64,57,67,58,67,65,67,2,5,18,22,8,11,20,16,3,64,31,7,1" +
",4,6,17,64,13,12,9,14,21,15,23,10,64,55,67,56,67:2,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,287,
"0,1,2,1,3,1:11,4,5,1:4,6,1:2,7,6:8,1,6:11,8,1:2,9,10,1:31,11,12,13,14,15,16" +
",1,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40," +
"41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65," +
"66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90," +
"91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111," +
"112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,127,128,129,130" +
",131,132,133,134,135,136,137,138,139,140,141,142,143,144,145,146,147,148,14" +
"9,150,151,152,153,154,6,155,156,157,158,159,160,161,162,163,164,165,166,167" +
",168,169,170,171,172,173,174,175,176,177,178,179,180,181,182,183,184,185,18" +
"6,187,6,188,189,190,191,192,193,194,195,196,197,198,199,200,201,202,203,204" +
",205,206,207,208,209,210,211,212")[0];

	private int yy_nxt[][] = unpackFromString(213,71,
"1,2,261,83,200,270,261,272,274,275,261,276,277,278,261,279,261,280,281,282," +
"261,283,261:2,3,284,261:20,4,261,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,4," +
"261:2,84,88,20,85,21,-1:72,261,218,261:5,225,261:15,-1,261:21,227,261,-1:15" +
",227,261,227,-1:51,4,-1:16,4,-1:66,23,-1:71,24,-1:11,261:23,-1,261:21,227,2" +
"61,-1:15,227,261,227,-1:6,25:67,-1:2,25,-1:24,139,-1:70,144,-1:70,157,-1:47" +
",82:48,86,82:21,-1,228,261:2,87,261:6,22,261:12,-1,261:21,227,261,-1:15,227" +
",261,227,-1:54,82,-1:16,25,-1:72,20,-1:3,89:48,86,89:16,34,89:4,-1,261:8,26" +
",261:14,-1,261:21,227,261,-1:15,227,261,227,-1:6,89:48,86,89:21,-1,261:14,2" +
"7,261:8,-1,261:21,227,261,-1:15,227,261,227,-1:30,95,-1:46,261:3,28,261:19," +
"-1,261:21,227,261,-1:15,227,261,227,-1:14,97,-1:2,201,-1:5,99,-1,219,-1,226" +
",-1:9,101,202,-1:10,262,-1:27,261:7,29,261:15,-1,261:21,227,261,-1:15,227,2" +
"61,227,-1:13,103,-1:63,261:7,30,261:15,-1,261:21,227,261,-1:15,227,261,227," +
"-1:11,105,-1:65,261:7,31,261:15,-1,261:21,227,261,-1:15,227,261,227,-1:21,1" +
"09,-1:55,261:11,32,261:11,-1,261:21,227,261,-1:15,227,261,227,-1:19,221,-1:" +
"57,261:21,33,261,-1,261:21,227,261,-1:15,227,261,227,-1:25,203,-1:51,261:7," +
"35,261:15,-1,261:21,227,261,-1:15,227,261,227,-1:9,206,-1:67,261:7,36,261:1" +
"5,-1,261:21,227,261,-1:15,227,261,227,-1:14,113,-1:62,261:11,37,261:11,-1,2" +
"61:21,227,261,-1:15,227,261,227,-1:13,115,-1:63,261:23,91,261:21,227,261,-1" +
":15,227,261,227,-1:12,119,-1:64,261:8,38,261:14,-1,261:21,227,261,-1:15,227" +
",261,227,-1:31,130,131,-1:44,261:15,39,261:7,-1,261:21,227,261,-1:15,227,26" +
"1,227,-1:23,132,-1:53,261:17,40,261:5,-1,261:21,227,261,-1:15,227,261,227,-" +
"1:31,285,-1:45,261:3,41,261:19,-1,261:21,227,261,-1:15,227,261,227,-1:7,133" +
",-1:69,261:17,42,261:5,-1,261:21,227,261,-1:15,227,261,227,-1:14,208,-1:62," +
"261:19,43,261:3,-1,261:21,227,261,-1:15,227,261,227,-1:11,134,-1:65,261:23," +
"93,261:21,227,261,-1:15,227,261,227,-1:13,46,-1:63,261:3,44,261:19,-1,261:2" +
"1,227,261,-1:15,227,261,227,-1:15,47,-1:61,261:11,45,261:11,-1,261:21,227,2" +
"61,-1:15,227,261,227,-1:24,135,-1:54,136,-1:81,209,-1:87,137,-1:49,48,-1:73" +
",49,-1:66,140,-1:84,141,-1:79,143,-1:42,50,-1:93,145,-1,146,147,-1,148,149," +
"-1:4,150,-1:8,151,-1:25,152,-1:76,153,-1:90,154,-1:56,155,-1:86,158,-1,159," +
"-1,211,-1,160,-1,213,222,161,-1,162,-1:40,163,-1:33,51,-1:70,52,-1:36,164,-" +
"1:33,53,-1:70,54,-1:36,165,-1:33,55,-1:36,166,-1:33,56,-1:36,167,-1:33,57,-" +
"1:37,168,-1:64,58,-1:92,169,-1:50,212,-1:68,59,-1:90,60,170,-1,171,-1,61,-1" +
",172,-1:6,173,-1:56,223,-1:71,174,-1:76,176,-1:81,214,-1:68,179,-1:73,62,-1" +
":70,63,-1:70,64,-1:70,65,-1:70,66,-1:33,67,-1:91,180,-1:48,181,-1:68,182,-1" +
":67,220,-1:84,68,-1:94,184,-1:59,69,-1:67,185,-1:72,216,-1:69,217,-1:78,187" +
",-1:46,70,-1:69,189,-1:74,190,-1:90,71,-1:64,72,-1:78,192,-1:77,195,-1:54,2" +
"24,-1:46,73,-1:75,74,-1:65,75,-1:82,196,-1:92,197,-1:69,76,-1:69,198,-1:63," +
"77,-1:50,78,-1:90,79,-1:81,80,-1:74,81,-1:27,261:7,90,261:15,-1,261:21,227," +
"261,-1:15,227,261,227,-1:13,107,-1:68,207,-1:84,127,-1:54,125,-1:75,117,-1:" +
"69,129,-1:69,123,-1:69,138,-1:87,142,-1:55,156,-1:91,175,-1:50,215,-1:95,17" +
"7,-1:79,186,-1:33,188,-1:93,193,-1:69,194,-1:43,261:2,92,261:20,-1,261:21,2" +
"27,261,-1:15,227,261,227,-1:13,205,-1:82,191,-1:59,121,-1:90,178,-1:75,183," +
"-1:65,199,-1:42,261:19,264,261:3,-1,261:21,227,261,-1:15,227,261,227,-1:13," +
"111,-1:63,261:16,265,261:6,-1,261:21,227,261,-1:15,227,261,227,-1:6,261:5,2" +
"67,261:17,-1,261:21,227,261,-1:15,227,261,227,-1:6,261:8,94,261:14,-1,261:2" +
"1,227,261,-1:15,227,261,227,-1:6,261:3,271,261:19,-1,261:21,227,261,-1:15,2" +
"27,261,227,-1:6,261:11,96,261:11,-1,261:21,227,261,-1:15,227,261,227,-1:6,2" +
"61:8,242,261:14,-1,261:21,227,261,-1:15,227,261,227,-1:6,261:13,98,261:9,-1" +
",261:21,227,261,-1:15,227,261,227,-1:6,261:2,100,261:20,-1,261:21,227,261,-" +
"1:15,227,261,227,-1:6,261:6,243,261:16,-1,261:21,227,261,-1:15,227,261,227," +
"-1:6,261,244,261:21,-1,261:21,227,261,-1:15,227,261,227,-1:6,261:2,246,261:" +
"20,-1,261:21,227,261,-1:15,227,261,227,-1:6,261:4,247,261:18,-1,261:21,227," +
"261,-1:15,227,261,227,-1:6,261:12,249,261:10,-1,261:21,227,261,-1:15,227,26" +
"1,227,-1:6,261:2,102,261:20,-1,261:21,227,261,-1:15,227,261,227,-1:6,261:7," +
"253,261:15,-1,261:21,227,261,-1:15,227,261,227,-1:6,261:11,104,261:11,-1,26" +
"1:21,227,261,-1:15,227,261,227,-1:6,261:8,254,261:14,-1,261:21,227,261,-1:1" +
"5,227,261,227,-1:6,261:13,255,261:9,-1,261:21,227,261,-1:15,227,261,227,-1:" +
"6,261:6,106,261:16,-1,261:21,227,261,-1:15,227,261,227,-1:6,261:6,256,261:1" +
"6,-1,261:21,227,261,-1:15,227,261,227,-1:6,261:11,108,261:11,-1,261:21,227," +
"261,-1:15,227,261,227,-1:6,261:2,257,261:20,-1,261:21,227,261,-1:15,227,261" +
",227,-1:6,261:9,110,261:13,-1,261:21,227,261,-1:15,227,261,227,-1:6,261:12," +
"112,261:10,-1,261:21,227,261,-1:15,227,261,227,-1:6,261:8,114,261:14,-1,261" +
":21,227,261,-1:15,227,261,227,-1:6,261:3,260,261:19,-1,261:21,227,261,-1:15" +
",227,261,227,-1:6,261:2,116,261:20,-1,261:21,227,261,-1:15,227,261,227,-1:6" +
",261:12,118,261:10,-1,261:21,227,261,-1:15,227,261,227,-1:6,261:2,120,261:2" +
"0,-1,261:21,227,261,-1:15,227,261,227,-1:6,261:3,122,261:19,-1,261:21,227,2" +
"61,-1:15,227,261,227,-1:6,261:9,124,261:13,-1,261:21,227,261,-1:15,227,261," +
"227,-1:6,261,126,261:21,-1,261:21,227,261,-1:15,227,261,227,-1:6,261:21,128" +
",261,-1,261:21,227,261,-1:15,227,261,227,-1:11,204,-1:87,210,-1:48,261:19,2" +
"50,261:3,-1,261:21,227,261,-1:15,227,261,227,-1:6,261:5,251,261:17,-1,261:2" +
"1,227,261,-1:15,227,261,227,-1:6,261:8,245,261:14,-1,261:21,227,261,-1:15,2" +
"27,261,227,-1:6,261:6,269,261:16,-1,261:21,227,261,-1:15,227,261,227,-1:6,2" +
"61,248,261:21,-1,261:21,227,261,-1:15,227,261,227,-1:6,261:7,259,261:15,-1," +
"261:21,227,261,-1:15,227,261,227,-1:6,261:5,229,261:3,230,261:13,-1,261:21," +
"227,261,-1:15,227,261,227,-1:6,261:19,252,261:3,-1,261:21,227,261,-1:15,227" +
",261,227,-1:6,261:7,231,261:15,-1,261:21,227,261,-1:15,227,261,227,-1:6,261" +
":19,258,261:3,-1,261:21,227,261,-1:15,227,261,227,-1:6,261:6,232,261:15,233" +
",-1,261:21,227,261,-1:15,227,261,227,-1:6,261:12,234,261:2,235,261:7,-1,261" +
":21,227,261,-1:15,227,261,227,-1:6,261,236,261:21,-1,261:21,227,261,-1:15,2" +
"27,261,227,-1:6,261:8,237,261:14,-1,261:21,227,261,-1:15,227,261,227,-1:6,2" +
"61:7,266,261:15,-1,261:21,227,261,-1:15,227,261,227,-1:6,261:15,238,261:7,-" +
"1,261:21,227,261,-1:15,227,261,227,-1:6,261:13,239,261:9,-1,261:21,227,261," +
"-1:15,227,261,227,-1:6,261:6,268,261:16,-1,261:21,227,261,-1:15,227,261,227" +
",-1:6,261:8,240,261:14,-1,261:21,227,261,-1:15,227,261,227,-1:6,261:5,241,2" +
"61:17,-1,261:21,227,261,-1:15,227,261,227,-1:6,261:7,286,261:15,-1,261:21,2" +
"27,261,-1:15,227,261,227,-1:8,263,-1:68,261:19,273,261:3,-1,261:21,227,261," +
"-1:15,227,261,227,-1:5");

	public java_cup.runtime.Symbol next_token ()
		throws java.io.IOException {
		int yy_lookahead;
		int yy_anchor = YY_NO_ANCHOR;
		int yy_state = yy_state_dtrans[yy_lexical_state];
		int yy_next_state = YY_NO_STATE;
		int yy_last_accept_state = YY_NO_STATE;
		boolean yy_initial = true;
		int yy_this_accept;

		yy_mark_start();
		yy_this_accept = yy_acpt[yy_state];
		if (YY_NOT_ACCEPT != yy_this_accept) {
			yy_last_accept_state = yy_state;
			yy_mark_end();
		}
		while (true) {
			if (yy_initial && yy_at_bol) yy_lookahead = YY_BOL;
			else yy_lookahead = yy_advance();
			yy_next_state = YY_F;
			yy_next_state = yy_nxt[yy_rmap[yy_state]][yy_cmap[yy_lookahead]];
			if (YY_EOF == yy_lookahead && true == yy_initial) {

  return new Symbol(sym.EOF, new SymbolValue(yyline, yychar+1, "EOF"));
			}
			if (YY_F != yy_next_state) {
				yy_state = yy_next_state;
				yy_initial = false;
				yy_this_accept = yy_acpt[yy_state];
				if (YY_NOT_ACCEPT != yy_this_accept) {
					yy_last_accept_state = yy_state;
					yy_mark_end();
				}
			}
			else {
				if (YY_NO_STATE == yy_last_accept_state) {
					throw (new Error("Lexical Error: Unmatched Input."));
				}
				else {
					yy_anchor = yy_acpt[yy_last_accept_state];
					if (0 != (YY_END & yy_anchor)) {
						yy_move_end();
					}
					yy_to_mark();
					switch (yy_last_accept_state) {
					case 1:
						
					case -2:
						break;
					case 2:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -3:
						break;
					case 3:
						{return new Symbol(sym.DOT,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -4:
						break;
					case 4:
						{return new Symbol(sym.INT_LITERAL, new SymbolValue(yyline+1, yychar+1, yytext(), new Integer(yytext())));}
					case -5:
						break;
					case 5:
						{return new Symbol(sym.PLUS,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -6:
						break;
					case 6:
						{return new Symbol(sym.TIMES,new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -7:
						break;
					case 7:
						{return new Symbol(sym.MINUS,new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -8:
						break;
					case 8:
						{return new Symbol(sym.COMMA,new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -9:
						break;
					case 9:
						{return new Symbol(sym.SEMI,new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -10:
						break;
					case 10:
						{return new Symbol(sym.LPAREN,new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -11:
						break;
					case 11:
						{return new Symbol(sym.RPAREN,new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -12:
						break;
					case 12:
						{return new Symbol(sym.LBRACE,new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -13:
						break;
					case 13:
						{return new Symbol(sym.RBRACE,new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -14:
						break;
					case 14:
						{return new Symbol(sym.LBRACKET,new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -15:
						break;
					case 15:
						{return new Symbol(sym.RBRACKET,new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -16:
						break;
					case 16:
						{return new Symbol(sym.ASSIGN,new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -17:
						break;
					case 17:
						{ System.err.println("Illegal character: "+yytext()); }
					case -18:
						break;
					case 18:
						{return new Symbol(sym.LT,new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -19:
						break;
					case 19:
						{return new Symbol(sym.NOT,new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -20:
						break;
					case 20:
						{/*reset pos to -1, if 0, otherwise line 1 starts at 0, rest start at 1 */ yychar=-1;}
					case -21:
						break;
					case 21:
						{ /* ignore white space. */ }
					case -22:
						break;
					case 22:
						{return new Symbol(sym.IF,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -23:
						break;
					case 23:
						{return new Symbol(sym.EQUAL,new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -24:
						break;
					case 24:
						{return new Symbol(sym.AND,new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -25:
						break;
					case 25:
						{/* ignore everything*/}
					case -26:
						break;
					case 26:
						{return new Symbol(sym.INT,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -27:
						break;
					case 27:
						{return new Symbol(sym.NEW,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -28:
						break;
					case 28:
						{return new Symbol(sym.MAIN,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -29:
						break;
					case 29:
						{return new Symbol(sym.BYTE,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -30:
						break;
					case 30:
						{return new Symbol(sym.ELSE,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -31:
						break;
					case 31:
						{return new Symbol(sym.TRUE,new SymbolValue(yyline+1, yychar+1, yytext(), 1));}
					case -32:
						break;
					case 32:
						{return new Symbol(sym.THIS,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -33:
						break;
					case 33:
						{return new Symbol(sym.VOID,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -34:
						break;
					case 34:
						{/* ignore everything*/}
					case -35:
						break;
					case 35:
						{return new Symbol(sym.FALSE,new SymbolValue(yyline+1, yychar+1, yytext(), 0));}
					case -36:
						break;
					case 36:
						{return new Symbol(sym.WHILE,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -37:
						break;
					case 37:
						{return new Symbol(sym.CLASS,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -38:
						break;
					case 38:
						{return new Symbol(sym.IMPORT,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -39:
						break;
					case 39:
						{return new Symbol(sym.LENGTH,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -40:
						break;
					case 40:
						{return new Symbol(sym.STATIC,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -41:
						break;
					case 41:
						{return new Symbol(sym.RETURN,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -42:
						break;
					case 42:
						{return new Symbol(sym.PUBLIC,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -43:
						break;
					case 43:
						{return new Symbol(sym.STRING,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -44:
						break;
					case 44:
						{return new Symbol(sym.BOOLEAN,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -45:
						break;
					case 45:
						{return new Symbol(sym.EXTENDS,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -46:
						break;
					case 46:
						{return new Symbol(sym.MEGGYTONE,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -47:
						break;
					case 47:
						{return new Symbol(sym.MEGGY,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -48:
						break;
					case 48:
						{return new Symbol(sym.MEGGYDELAY,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -49:
						break;
					case 49:
						{return new Symbol(sym.MEGGYCOLOR,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -50:
						break;
					case 50:
						{return new Symbol(sym.MEGGYBUTTON,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -51:
						break;
					case 51:
						{return new Symbol(sym.TONE_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 36363));}
					case -52:
						break;
					case 52:
						{return new Symbol(sym.TONE_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 48541));}
					case -53:
						break;
					case 53:
						{return new Symbol(sym.TONE_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 54485));}
					case -54:
						break;
					case 54:
						{return new Symbol(sym.TONE_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 32397));}
					case -55:
						break;
					case 55:
						{return new Symbol(sym.TONE_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 61157));}
					case -56:
						break;
					case 56:
						{return new Symbol(sym.TONE_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 40816));}
					case -57:
						break;
					case 57:
						{return new Symbol(sym.TONE_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 45816));}
					case -58:
						break;
					case 58:
						{return new Symbol(sym.MEGGYSETPIXEL,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -59:
						break;
					case 59:
						{return new Symbol(sym.MEGGYGETPIXEL,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -60:
						break;
					case 60:
						{return new Symbol(sym.BUTTON_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 2));}
					case -61:
						break;
					case 61:
						{return new Symbol(sym.BUTTON_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 1));}
					case -62:
						break;
					case 62:
						{return new Symbol(sym.TONE_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 34323));}
					case -63:
						break;
					case 63:
						{return new Symbol(sym.TONE_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 51427));}
					case -64:
						break;
					case 64:
						{return new Symbol(sym.TONE_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 57724));}
					case -65:
						break;
					case 65:
						{return new Symbol(sym.TONE_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 38526));}
					case -66:
						break;
					case 66:
						{return new Symbol(sym.TONE_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 43243));}
					case -67:
						break;
					case 67:
						{return new Symbol(sym.MEGGYTONESTART,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -68:
						break;
					case 68:
						{return new Symbol(sym.BUTTON_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 4));}
					case -69:
						break;
					case 69:
						{return new Symbol(sym.COLOR_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 1));}
					case -70:
						break;
					case 70:
						{return new Symbol(sym.MEGGYSETAUXLEDS,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -71:
						break;
					case 71:
						{return new Symbol(sym.COLOR_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 0));}
					case -72:
						break;
					case 72:
						{return new Symbol(sym.COLOR_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 5));}
					case -73:
						break;
					case 73:
						{return new Symbol(sym.MEGGYCHECKBUTTON,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -74:
						break;
					case 74:
						{return new Symbol(sym.BUTTON_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 16));}
					case -75:
						break;
					case 75:
						{return new Symbol(sym.BUTTON_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 8));}
					case -76:
						break;
					case 76:
						{return new Symbol(sym.COLOR_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 4));}
					case -77:
						break;
					case 77:
						{return new Symbol(sym.COLOR_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 7));}
					case -78:
						break;
					case 78:
						{return new Symbol(sym.BUTTON_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 32));}
					case -79:
						break;
					case 79:
						{return new Symbol(sym.COLOR_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 2));}
					case -80:
						break;
					case 80:
						{return new Symbol(sym.COLOR_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 3));}
					case -81:
						break;
					case 81:
						{return new Symbol(sym.COLOR_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 6));}
					case -82:
						break;
					case 83:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -83:
						break;
					case 84:
						{ System.err.println("Illegal character: "+yytext()); }
					case -84:
						break;
					case 85:
						{/*reset pos to -1, if 0, otherwise line 1 starts at 0, rest start at 1 */ yychar=-1;}
					case -85:
						break;
					case 87:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -86:
						break;
					case 88:
						{ System.err.println("Illegal character: "+yytext()); }
					case -87:
						break;
					case 90:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -88:
						break;
					case 92:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -89:
						break;
					case 94:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -90:
						break;
					case 96:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -91:
						break;
					case 98:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -92:
						break;
					case 100:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -93:
						break;
					case 102:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -94:
						break;
					case 104:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -95:
						break;
					case 106:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -96:
						break;
					case 108:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -97:
						break;
					case 110:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -98:
						break;
					case 112:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -99:
						break;
					case 114:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -100:
						break;
					case 116:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -101:
						break;
					case 118:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -102:
						break;
					case 120:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -103:
						break;
					case 122:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -104:
						break;
					case 124:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -105:
						break;
					case 126:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -106:
						break;
					case 128:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -107:
						break;
					case 200:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -108:
						break;
					case 218:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -109:
						break;
					case 225:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -110:
						break;
					case 227:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -111:
						break;
					case 228:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -112:
						break;
					case 229:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -113:
						break;
					case 230:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -114:
						break;
					case 231:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -115:
						break;
					case 232:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -116:
						break;
					case 233:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -117:
						break;
					case 234:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -118:
						break;
					case 235:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -119:
						break;
					case 236:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -120:
						break;
					case 237:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -121:
						break;
					case 238:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -122:
						break;
					case 239:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -123:
						break;
					case 240:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -124:
						break;
					case 241:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -125:
						break;
					case 242:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -126:
						break;
					case 243:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -127:
						break;
					case 244:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -128:
						break;
					case 245:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -129:
						break;
					case 246:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -130:
						break;
					case 247:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -131:
						break;
					case 248:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -132:
						break;
					case 249:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -133:
						break;
					case 250:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -134:
						break;
					case 251:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -135:
						break;
					case 252:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -136:
						break;
					case 253:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -137:
						break;
					case 254:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -138:
						break;
					case 255:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -139:
						break;
					case 256:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -140:
						break;
					case 257:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -141:
						break;
					case 258:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -142:
						break;
					case 259:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -143:
						break;
					case 260:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -144:
						break;
					case 261:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -145:
						break;
					case 264:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -146:
						break;
					case 265:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -147:
						break;
					case 266:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -148:
						break;
					case 267:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -149:
						break;
					case 268:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -150:
						break;
					case 269:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -151:
						break;
					case 270:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -152:
						break;
					case 271:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -153:
						break;
					case 272:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -154:
						break;
					case 273:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -155:
						break;
					case 274:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -156:
						break;
					case 275:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -157:
						break;
					case 276:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -158:
						break;
					case 277:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -159:
						break;
					case 278:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -160:
						break;
					case 279:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -161:
						break;
					case 280:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -162:
						break;
					case 281:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -163:
						break;
					case 282:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -164:
						break;
					case 283:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -165:
						break;
					case 284:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -166:
						break;
					case 286:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -167:
						break;
					default:
						yy_error(YY_E_INTERNAL,false);
					case -1:
					}
					yy_initial = true;
					yy_state = yy_state_dtrans[yy_lexical_state];
					yy_next_state = YY_NO_STATE;
					yy_last_accept_state = YY_NO_STATE;
					yy_mark_start();
					yy_this_accept = yy_acpt[yy_state];
					if (YY_NOT_ACCEPT != yy_this_accept) {
						yy_last_accept_state = yy_state;
						yy_mark_end();
					}
				}
			}
		}
	}
}
