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
		/* 71 */ YY_NOT_ACCEPT,
		/* 72 */ YY_NO_ANCHOR,
		/* 73 */ YY_NO_ANCHOR,
		/* 74 */ YY_NO_ANCHOR,
		/* 75 */ YY_NOT_ACCEPT,
		/* 76 */ YY_NO_ANCHOR,
		/* 77 */ YY_NOT_ACCEPT,
		/* 78 */ YY_NO_ANCHOR,
		/* 79 */ YY_NOT_ACCEPT,
		/* 80 */ YY_NO_ANCHOR,
		/* 81 */ YY_NOT_ACCEPT,
		/* 82 */ YY_NO_ANCHOR,
		/* 83 */ YY_NOT_ACCEPT,
		/* 84 */ YY_NO_ANCHOR,
		/* 85 */ YY_NOT_ACCEPT,
		/* 86 */ YY_NO_ANCHOR,
		/* 87 */ YY_NOT_ACCEPT,
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
		/* 118 */ YY_NOT_ACCEPT,
		/* 119 */ YY_NOT_ACCEPT,
		/* 120 */ YY_NOT_ACCEPT,
		/* 121 */ YY_NOT_ACCEPT,
		/* 122 */ YY_NOT_ACCEPT,
		/* 123 */ YY_NOT_ACCEPT,
		/* 124 */ YY_NOT_ACCEPT,
		/* 125 */ YY_NOT_ACCEPT,
		/* 126 */ YY_NOT_ACCEPT,
		/* 127 */ YY_NOT_ACCEPT,
		/* 128 */ YY_NOT_ACCEPT,
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
		/* 188 */ YY_NO_ANCHOR,
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
		/* 200 */ YY_NOT_ACCEPT,
		/* 201 */ YY_NOT_ACCEPT,
		/* 202 */ YY_NOT_ACCEPT,
		/* 203 */ YY_NOT_ACCEPT,
		/* 204 */ YY_NOT_ACCEPT,
		/* 205 */ YY_NOT_ACCEPT,
		/* 206 */ YY_NO_ANCHOR,
		/* 207 */ YY_NOT_ACCEPT,
		/* 208 */ YY_NOT_ACCEPT,
		/* 209 */ YY_NOT_ACCEPT,
		/* 210 */ YY_NOT_ACCEPT,
		/* 211 */ YY_NOT_ACCEPT,
		/* 212 */ YY_NOT_ACCEPT,
		/* 213 */ YY_NO_ANCHOR,
		/* 214 */ YY_NOT_ACCEPT,
		/* 215 */ YY_NO_ANCHOR,
		/* 216 */ YY_NO_ANCHOR,
		/* 217 */ YY_NO_ANCHOR,
		/* 218 */ YY_NO_ANCHOR,
		/* 219 */ YY_NO_ANCHOR,
		/* 220 */ YY_NO_ANCHOR,
		/* 221 */ YY_NO_ANCHOR,
		/* 222 */ YY_NO_ANCHOR,
		/* 223 */ YY_NO_ANCHOR,
		/* 224 */ YY_NO_ANCHOR,
		/* 225 */ YY_NO_ANCHOR,
		/* 226 */ YY_NO_ANCHOR,
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
		/* 250 */ YY_NOT_ACCEPT,
		/* 251 */ YY_NOT_ACCEPT,
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
		/* 262 */ YY_NO_ANCHOR,
		/* 263 */ YY_NO_ANCHOR,
		/* 264 */ YY_NO_ANCHOR,
		/* 265 */ YY_NO_ANCHOR,
		/* 266 */ YY_NO_ANCHOR,
		/* 267 */ YY_NO_ANCHOR,
		/* 268 */ YY_NO_ANCHOR,
		/* 269 */ YY_NO_ANCHOR,
		/* 270 */ YY_NO_ANCHOR,
		/* 271 */ YY_NO_ANCHOR,
		/* 272 */ YY_NO_ANCHOR,
		/* 273 */ YY_NOT_ACCEPT,
		/* 274 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"58:9,61,59,58,61,60,58:18,61,58:7,52,53,49,48,50,58,24,57,54:3,46,54:6,58,5" +
"1,58:5,27,32,33,30,29,47,38,45,43,55,35,28,25,37,36,26,55,34,19,44,41,42,40" +
",55,39,55,58:4,56,58,2,5,18,22,8,11,20,16,3,55,31,7,1,4,6,17,55,13,12,9,14," +
"21,15,23,10,55,58:5,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,275,
"0,1,2,1,3,1:8,4,5,4:8,1,4:11,6,1:2,7,8,1:31,9,10,11,12,13,14,15,16,17,18,19" +
",20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44" +
",45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69" +
",70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94" +
",95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114" +
",115,116,117,118,119,120,121,122,123,124,125,126,127,128,129,130,131,132,13" +
"3,134,135,136,137,138,139,140,141,142,143,144,145,146,147,148,149,150,151,1" +
"52,4,153,154,155,156,157,158,159,160,161,162,163,164,165,166,167,168,169,17" +
"0,171,172,173,174,175,176,177,178,179,180,181,182,183,184,185,4,186,187,188" +
",189,190,191,192,193,194,195,196,197,198,199,200,201,202,203,204,205,206,20" +
"7,208,209,210")[0];

	private int yy_nxt[][] = unpackFromString(211,62,
"1,2,249,72,188,258,249,260,262,263,249,264,265,266,249,267,249,268,269,270," +
"249,271,249:2,3,272,249:20,4,249,5,6,7,8,9,10,4,249:2,73,3,11,74,12,-1:63,2" +
"49,206,249:5,213,249:15,-1,249:21,215,249,-1:6,215,249,215,-1:51,4,-1:7,4,-" +
"1:8,249:23,-1,249:21,215,249,-1:6,215,249,215,-1:6,14:58,-1:2,14,-1:24,127," +
"-1:61,132,-1:61,145,-1:38,71:48,75,71:12,-1,216,249:2,76,249:6,13,249:12,-1" +
",249:21,215,249,-1:6,215,249,215,-1:54,71,-1:7,14,-1:63,11,-1:3,77:48,75,77" +
":7,23,77:4,-1,249:8,15,249:14,-1,249:21,215,249,-1:6,215,249,215,-1:6,77:48" +
",75,77:12,-1,249:14,16,249:8,-1,249:21,215,249,-1:6,215,249,215,-1:30,83,-1" +
":37,249:3,17,249:19,-1,249:21,215,249,-1:6,215,249,215,-1:14,85,-1:2,189,-1" +
":5,87,-1,207,-1,214,-1:9,89,190,-1:10,250,-1:18,249:7,18,249:15,-1,249:21,2" +
"15,249,-1:6,215,249,215,-1:13,91,-1:54,249:7,19,249:15,-1,249:21,215,249,-1" +
":6,215,249,215,-1:11,93,-1:56,249:7,20,249:15,-1,249:21,215,249,-1:6,215,24" +
"9,215,-1:21,97,-1:46,249:11,21,249:11,-1,249:21,215,249,-1:6,215,249,215,-1" +
":19,209,-1:48,249:21,22,249,-1,249:21,215,249,-1:6,215,249,215,-1:25,191,-1" +
":42,249:7,24,249:15,-1,249:21,215,249,-1:6,215,249,215,-1:9,194,-1:58,249:7" +
",25,249:15,-1,249:21,215,249,-1:6,215,249,215,-1:14,101,-1:53,249:11,26,249" +
":11,-1,249:21,215,249,-1:6,215,249,215,-1:13,103,-1:54,249:23,79,249:21,215" +
",249,-1:6,215,249,215,-1:12,107,-1:55,249:8,27,249:14,-1,249:21,215,249,-1:" +
"6,215,249,215,-1:31,118,119,-1:35,249:15,28,249:7,-1,249:21,215,249,-1:6,21" +
"5,249,215,-1:23,120,-1:44,249:17,29,249:5,-1,249:21,215,249,-1:6,215,249,21" +
"5,-1:31,273,-1:36,249:3,30,249:19,-1,249:21,215,249,-1:6,215,249,215,-1:7,1" +
"21,-1:60,249:17,31,249:5,-1,249:21,215,249,-1:6,215,249,215,-1:14,196,-1:53" +
",249:19,32,249:3,-1,249:21,215,249,-1:6,215,249,215,-1:11,122,-1:56,249:23," +
"81,249:21,215,249,-1:6,215,249,215,-1:13,35,-1:54,249:3,33,249:19,-1,249:21" +
",215,249,-1:6,215,249,215,-1:15,36,-1:52,249:11,34,249:11,-1,249:21,215,249" +
",-1:6,215,249,215,-1:24,123,-1:45,124,-1:72,197,-1:78,125,-1:40,37,-1:64,38" +
",-1:57,128,-1:75,129,-1:70,131,-1:33,39,-1:84,133,-1,134,135,-1,136,137,-1:" +
"4,138,-1:8,139,-1:16,140,-1:67,141,-1:81,142,-1:47,143,-1:77,146,-1,147,-1," +
"199,-1,148,-1,201,210,149,-1,150,-1:31,151,-1:33,40,-1:61,41,-1:27,152,-1:3" +
"3,42,-1:61,43,-1:27,153,-1:33,44,-1:27,154,-1:33,45,-1:27,155,-1:33,46,-1:2" +
"8,156,-1:55,47,-1:83,157,-1:41,200,-1:59,48,-1:81,49,158,-1,159,-1,50,-1,16" +
"0,-1:6,161,-1:47,211,-1:62,162,-1:67,164,-1:72,202,-1:59,167,-1:64,51,-1:61" +
",52,-1:61,53,-1:61,54,-1:61,55,-1:24,56,-1:82,168,-1:39,169,-1:59,170,-1:58" +
",208,-1:75,57,-1:85,172,-1:50,58,-1:58,173,-1:63,204,-1:60,205,-1:69,175,-1" +
":37,59,-1:60,177,-1:65,178,-1:81,60,-1:55,61,-1:69,180,-1:68,183,-1:45,212," +
"-1:37,62,-1:66,63,-1:56,64,-1:73,184,-1:83,185,-1:60,65,-1:60,186,-1:54,66," +
"-1:41,67,-1:81,68,-1:72,69,-1:65,70,-1:18,249:7,78,249:15,-1,249:21,215,249" +
",-1:6,215,249,215,-1:13,95,-1:59,195,-1:75,115,-1:45,113,-1:66,105,-1:60,11" +
"7,-1:60,111,-1:60,126,-1:78,130,-1:46,144,-1:82,163,-1:41,203,-1:86,165,-1:" +
"70,174,-1:24,176,-1:84,181,-1:60,182,-1:34,249:2,80,249:20,-1,249:21,215,24" +
"9,-1:6,215,249,215,-1:13,193,-1:73,179,-1:50,109,-1:81,166,-1:66,171,-1:56," +
"187,-1:33,249:19,252,249:3,-1,249:21,215,249,-1:6,215,249,215,-1:13,99,-1:5" +
"4,249:16,253,249:6,-1,249:21,215,249,-1:6,215,249,215,-1:6,249:5,255,249:17" +
",-1,249:21,215,249,-1:6,215,249,215,-1:6,249:8,82,249:14,-1,249:21,215,249," +
"-1:6,215,249,215,-1:6,249:3,259,249:19,-1,249:21,215,249,-1:6,215,249,215,-" +
"1:6,249:11,84,249:11,-1,249:21,215,249,-1:6,215,249,215,-1:6,249:8,230,249:" +
"14,-1,249:21,215,249,-1:6,215,249,215,-1:6,249:13,86,249:9,-1,249:21,215,24" +
"9,-1:6,215,249,215,-1:6,249:2,88,249:20,-1,249:21,215,249,-1:6,215,249,215," +
"-1:6,249:6,231,249:16,-1,249:21,215,249,-1:6,215,249,215,-1:6,249,232,249:2" +
"1,-1,249:21,215,249,-1:6,215,249,215,-1:6,249:2,234,249:20,-1,249:21,215,24" +
"9,-1:6,215,249,215,-1:6,249:4,235,249:18,-1,249:21,215,249,-1:6,215,249,215" +
",-1:6,249:12,237,249:10,-1,249:21,215,249,-1:6,215,249,215,-1:6,249:2,90,24" +
"9:20,-1,249:21,215,249,-1:6,215,249,215,-1:6,249:7,241,249:15,-1,249:21,215" +
",249,-1:6,215,249,215,-1:6,249:11,92,249:11,-1,249:21,215,249,-1:6,215,249," +
"215,-1:6,249:8,242,249:14,-1,249:21,215,249,-1:6,215,249,215,-1:6,249:13,24" +
"3,249:9,-1,249:21,215,249,-1:6,215,249,215,-1:6,249:6,94,249:16,-1,249:21,2" +
"15,249,-1:6,215,249,215,-1:6,249:6,244,249:16,-1,249:21,215,249,-1:6,215,24" +
"9,215,-1:6,249:11,96,249:11,-1,249:21,215,249,-1:6,215,249,215,-1:6,249:2,2" +
"45,249:20,-1,249:21,215,249,-1:6,215,249,215,-1:6,249:9,98,249:13,-1,249:21" +
",215,249,-1:6,215,249,215,-1:6,249:12,100,249:10,-1,249:21,215,249,-1:6,215" +
",249,215,-1:6,249:8,102,249:14,-1,249:21,215,249,-1:6,215,249,215,-1:6,249:" +
"3,248,249:19,-1,249:21,215,249,-1:6,215,249,215,-1:6,249:2,104,249:20,-1,24" +
"9:21,215,249,-1:6,215,249,215,-1:6,249:12,106,249:10,-1,249:21,215,249,-1:6" +
",215,249,215,-1:6,249:2,108,249:20,-1,249:21,215,249,-1:6,215,249,215,-1:6," +
"249:3,110,249:19,-1,249:21,215,249,-1:6,215,249,215,-1:6,249:9,112,249:13,-" +
"1,249:21,215,249,-1:6,215,249,215,-1:6,249,114,249:21,-1,249:21,215,249,-1:" +
"6,215,249,215,-1:6,249:21,116,249,-1,249:21,215,249,-1:6,215,249,215,-1:11," +
"192,-1:78,198,-1:39,249:19,238,249:3,-1,249:21,215,249,-1:6,215,249,215,-1:" +
"6,249:5,239,249:17,-1,249:21,215,249,-1:6,215,249,215,-1:6,249:8,233,249:14" +
",-1,249:21,215,249,-1:6,215,249,215,-1:6,249:6,257,249:16,-1,249:21,215,249" +
",-1:6,215,249,215,-1:6,249,236,249:21,-1,249:21,215,249,-1:6,215,249,215,-1" +
":6,249:7,247,249:15,-1,249:21,215,249,-1:6,215,249,215,-1:6,249:5,217,249:3" +
",218,249:13,-1,249:21,215,249,-1:6,215,249,215,-1:6,249:19,240,249:3,-1,249" +
":21,215,249,-1:6,215,249,215,-1:6,249:7,219,249:15,-1,249:21,215,249,-1:6,2" +
"15,249,215,-1:6,249:19,246,249:3,-1,249:21,215,249,-1:6,215,249,215,-1:6,24" +
"9:6,220,249:15,221,-1,249:21,215,249,-1:6,215,249,215,-1:6,249:12,222,249:2" +
",223,249:7,-1,249:21,215,249,-1:6,215,249,215,-1:6,249,224,249:21,-1,249:21" +
",215,249,-1:6,215,249,215,-1:6,249:8,225,249:14,-1,249:21,215,249,-1:6,215," +
"249,215,-1:6,249:7,254,249:15,-1,249:21,215,249,-1:6,215,249,215,-1:6,249:1" +
"5,226,249:7,-1,249:21,215,249,-1:6,215,249,215,-1:6,249:13,227,249:9,-1,249" +
":21,215,249,-1:6,215,249,215,-1:6,249:6,256,249:16,-1,249:21,215,249,-1:6,2" +
"15,249,215,-1:6,249:8,228,249:14,-1,249:21,215,249,-1:6,215,249,215,-1:6,24" +
"9:5,229,249:17,-1,249:21,215,249,-1:6,215,249,215,-1:6,249:7,274,249:15,-1," +
"249:21,215,249,-1:6,215,249,215,-1:8,251,-1:59,249:19,261,249:3,-1,249:21,2" +
"15,249,-1:6,215,249,215,-1:5");

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
						{ System.err.println("Illegal character: "+yytext()); }
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
						{return new Symbol(sym.COMMA,new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -8:
						break;
					case 8:
						{return new Symbol(sym.SEMI,new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -9:
						break;
					case 9:
						{return new Symbol(sym.LPAREN,new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -10:
						break;
					case 10:
						{return new Symbol(sym.RPAREN,new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -11:
						break;
					case 11:
						{/*reset pos to -1, if 0, otherwise line 1 starts at 0, rest start at 1 */ yychar=-1;}
					case -12:
						break;
					case 12:
						{ /* ignore white space. */ }
					case -13:
						break;
					case 13:
						{return new Symbol(sym.IF,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -14:
						break;
					case 14:
						{/* ignore everything*/}
					case -15:
						break;
					case 15:
						{return new Symbol(sym.INT,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -16:
						break;
					case 16:
						{return new Symbol(sym.NEW,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -17:
						break;
					case 17:
						{return new Symbol(sym.MAIN,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -18:
						break;
					case 18:
						{return new Symbol(sym.BYTE,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -19:
						break;
					case 19:
						{return new Symbol(sym.ELSE,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -20:
						break;
					case 20:
						{return new Symbol(sym.TRUE,new SymbolValue(yyline+1, yychar+1, yytext(), 1));}
					case -21:
						break;
					case 21:
						{return new Symbol(sym.THIS,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -22:
						break;
					case 22:
						{return new Symbol(sym.VOID,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -23:
						break;
					case 23:
						{/* ignore everything*/}
					case -24:
						break;
					case 24:
						{return new Symbol(sym.FALSE,new SymbolValue(yyline+1, yychar+1, yytext(), 0));}
					case -25:
						break;
					case 25:
						{return new Symbol(sym.WHILE,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -26:
						break;
					case 26:
						{return new Symbol(sym.CLASS,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -27:
						break;
					case 27:
						{return new Symbol(sym.IMPORT,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -28:
						break;
					case 28:
						{return new Symbol(sym.LENGTH,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -29:
						break;
					case 29:
						{return new Symbol(sym.STATIC,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -30:
						break;
					case 30:
						{return new Symbol(sym.RETURN,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -31:
						break;
					case 31:
						{return new Symbol(sym.PUBLIC,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -32:
						break;
					case 32:
						{return new Symbol(sym.STRING,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -33:
						break;
					case 33:
						{return new Symbol(sym.BOOLEAN,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -34:
						break;
					case 34:
						{return new Symbol(sym.EXTENDS,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -35:
						break;
					case 35:
						{return new Symbol(sym.MEGGYTONE,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -36:
						break;
					case 36:
						{return new Symbol(sym.MEGGY,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -37:
						break;
					case 37:
						{return new Symbol(sym.MEGGYDELAY,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -38:
						break;
					case 38:
						{return new Symbol(sym.MEGGYCOLOR,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -39:
						break;
					case 39:
						{return new Symbol(sym.MEGGYBUTTON,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -40:
						break;
					case 40:
						{return new Symbol(sym.TONE_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 36363));}
					case -41:
						break;
					case 41:
						{return new Symbol(sym.TONE_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 48541));}
					case -42:
						break;
					case 42:
						{return new Symbol(sym.TONE_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 54485));}
					case -43:
						break;
					case 43:
						{return new Symbol(sym.TONE_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 32397));}
					case -44:
						break;
					case 44:
						{return new Symbol(sym.TONE_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 61157));}
					case -45:
						break;
					case 45:
						{return new Symbol(sym.TONE_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 40816));}
					case -46:
						break;
					case 46:
						{return new Symbol(sym.TONE_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 45816));}
					case -47:
						break;
					case 47:
						{return new Symbol(sym.MEGGYSETPIXEL,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -48:
						break;
					case 48:
						{return new Symbol(sym.MEGGYGETPIXEL,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -49:
						break;
					case 49:
						{return new Symbol(sym.BUTTON_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 2));}
					case -50:
						break;
					case 50:
						{return new Symbol(sym.BUTTON_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 1));}
					case -51:
						break;
					case 51:
						{return new Symbol(sym.TONE_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 34323));}
					case -52:
						break;
					case 52:
						{return new Symbol(sym.TONE_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 51427));}
					case -53:
						break;
					case 53:
						{return new Symbol(sym.TONE_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 57724));}
					case -54:
						break;
					case 54:
						{return new Symbol(sym.TONE_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 38526));}
					case -55:
						break;
					case 55:
						{return new Symbol(sym.TONE_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 43243));}
					case -56:
						break;
					case 56:
						{return new Symbol(sym.MEGGYTONESTART,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -57:
						break;
					case 57:
						{return new Symbol(sym.BUTTON_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 4));}
					case -58:
						break;
					case 58:
						{return new Symbol(sym.COLOR_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 1));}
					case -59:
						break;
					case 59:
						{return new Symbol(sym.MEGGYSETAUXLEDS,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -60:
						break;
					case 60:
						{return new Symbol(sym.COLOR_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 0));}
					case -61:
						break;
					case 61:
						{return new Symbol(sym.COLOR_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 5));}
					case -62:
						break;
					case 62:
						{return new Symbol(sym.MEGGYCHECKBUTTON,new SymbolValue(yyline+1, yychar+1, yytext()));}
					case -63:
						break;
					case 63:
						{return new Symbol(sym.BUTTON_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 16));}
					case -64:
						break;
					case 64:
						{return new Symbol(sym.BUTTON_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 8));}
					case -65:
						break;
					case 65:
						{return new Symbol(sym.COLOR_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 4));}
					case -66:
						break;
					case 66:
						{return new Symbol(sym.COLOR_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 7));}
					case -67:
						break;
					case 67:
						{return new Symbol(sym.BUTTON_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 32));}
					case -68:
						break;
					case 68:
						{return new Symbol(sym.COLOR_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 2));}
					case -69:
						break;
					case 69:
						{return new Symbol(sym.COLOR_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 3));}
					case -70:
						break;
					case 70:
						{return new Symbol(sym.COLOR_LITERAL,new SymbolValue(yyline+1, yychar+1, yytext(), 6));}
					case -71:
						break;
					case 72:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -72:
						break;
					case 73:
						{ System.err.println("Illegal character: "+yytext()); }
					case -73:
						break;
					case 74:
						{/*reset pos to -1, if 0, otherwise line 1 starts at 0, rest start at 1 */ yychar=-1;}
					case -74:
						break;
					case 76:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -75:
						break;
					case 78:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -76:
						break;
					case 80:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -77:
						break;
					case 82:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -78:
						break;
					case 84:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -79:
						break;
					case 86:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -80:
						break;
					case 88:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -81:
						break;
					case 90:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -82:
						break;
					case 92:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -83:
						break;
					case 94:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -84:
						break;
					case 96:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -85:
						break;
					case 98:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -86:
						break;
					case 100:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -87:
						break;
					case 102:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -88:
						break;
					case 104:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -89:
						break;
					case 106:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -90:
						break;
					case 108:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -91:
						break;
					case 110:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -92:
						break;
					case 112:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -93:
						break;
					case 114:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -94:
						break;
					case 116:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -95:
						break;
					case 188:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -96:
						break;
					case 206:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -97:
						break;
					case 213:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -98:
						break;
					case 215:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -99:
						break;
					case 216:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -100:
						break;
					case 217:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -101:
						break;
					case 218:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -102:
						break;
					case 219:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -103:
						break;
					case 220:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -104:
						break;
					case 221:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -105:
						break;
					case 222:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -106:
						break;
					case 223:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -107:
						break;
					case 224:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -108:
						break;
					case 225:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -109:
						break;
					case 226:
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
					case 252:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -134:
						break;
					case 253:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -135:
						break;
					case 254:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -136:
						break;
					case 255:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -137:
						break;
					case 256:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -138:
						break;
					case 257:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -139:
						break;
					case 258:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -140:
						break;
					case 259:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -141:
						break;
					case 260:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -142:
						break;
					case 261:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -143:
						break;
					case 262:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -144:
						break;
					case 263:
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
					case 274:
						{return new Symbol(sym.ID, new SymbolValue(yyline+1, yychar+1, yytext(), -1));}
					case -155:
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
