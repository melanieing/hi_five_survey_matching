package service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Banner {
	
	private int mode;
	
	public Banner(int mode) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
		this.mode = mode;
		if (this.mode == 0) {
			System.out.println("╭ ◜◝ ͡ ◜◝ ͡ ◜◝ ͡ ◜◝ ͡ ◜◝ ͡ ◜◝ ͡ ◜◝ ͡ ◜◝ ͡ ◜◝ ͡ ◜◝ ͡ ◜◝ ͡ ◜◝ ͡ ◜◝ ͡ ◜◝ ͡ ◜◝ ͡ ◜◝ ͡ ◜◝ ͡ ◜◝╮");
			System.out.println("       ♬안녕하세요, 설문조사 기반 친구 매칭 서비스 Hi-Five!에 오신 걸 환영합니다!");
			System.out.println("╰ ◟◞ ͜ ◟◞ ͜ ◟◞ ͜ ◟◞ ͜ ◟◞ ͜ ◟◞ ͜ ◟◞ ͜ ◟◞ ͜ ◟◞ ͜ ◟◞ ͜ ◟◞ ͜ ◟◞ ͜ ◟◞ ͜ ◟◞ ͜ ◟◞ ͜ ◟◞ ͜ ◟◞ ͜ ◟◞╯");
		} else if (this.mode == 1) { // 관리자 모드 배너
			System.out.println("╭ ◜◝ ͡ ◜◝ ͡ ◜◝ ͡ ◜◝ ͡ ◜◝ ͡ ◜◝ ͡ ◜◝ ͡ ◜◝ ͡ ◜◝ ͡ ◜◝ ͡ ◜◝ ͡ ◜◝ ͡ ◜◝╮");
			System.out.printf("        관리자 님 ! 오늘은 " + LocalDate.now().format(formatter) + "입니다.\n");
			System.out.println("╰ ◟◞ ͜ ◟◞ ͜ ◟◞ ͜ ◟◞ ͜ ◟◞ ͜ ◟◞ ͜ ◟◞ ͜ ◟◞ ͜ ◟◞ ͜ ◟◞ ͜ ◟◞ ͜ ◟◞ ͜ ◟◞╯");
			System.out.println("O °");
			System.out.println(" ᕱ ᕱ       \"Hi-Five!\"");
			System.out.println("( ･ω･)      ");
			System.out.println("/ つΦ . .. . ﹢ ⃰ ଂ ಇ	");	
		} else if (this.mode == 2) { // 사용자 모드 배너
			System.out.println("╭ ◜◝ ͡ ◜◝ ͡ ◜◝ ͡ ◜◝ ͡ ◜◝ ͡ ◜◝ ͡ ◜◝ ͡ ◜◝ ͡ ◜◝ ͡ ◜◝ ͡ ◜◝ ͡ ◜◝ ͡ ◜◝ ͡ ◜◝╮");
			System.out.println("   설문에 참여하시면, 당신과 비슷한 성향의 친구를 매칭해드립니다! ");
			System.out.println("╰ ◟◞ ͜ ◟◞ ͜ ◟◞ ͜ ◟◞ ͜ ◟◞ ͜ ◟◞ ͜ ◟◞ ͜ ◟◞ ͜ ◟◞ ͜ ◟◞ ͜ ◟◞ ͜ ◟◞ ͜ ◟◞ ͜ ◟◞╯");
			System.out.println("O °");
			System.out.println(" ᕱ ᕱ       \"Hi-Five!\"");
			System.out.println("( ･ω･)      ");
			System.out.println("/ つΦ . .. . ﹢ ⃰ ଂ ಇ	");	
		}
		
	}
	
}
