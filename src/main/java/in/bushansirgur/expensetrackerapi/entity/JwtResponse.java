package in.bushansirgur.expensetrackerapi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtResponse {
	
	private final String token;
	private final String email;
	
}
