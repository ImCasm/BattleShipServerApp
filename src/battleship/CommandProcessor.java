package battleship;

/**
 *
 * @author Cristian
 */
public class CommandProcessor {

    private BattleServerController player1;
    private BattleServerController player2;

    private boolean registerPlayer1(BattleServerController bsc) {
        if (player1 == null) {
            player1 = bsc;
            return true;
        }

        return false;
    }

    private boolean registerPlayer2(BattleServerController bsc) {

        if (player2 == null) {
            player2 = bsc;
            return true;
        }

        return false;
    }

    public boolean remove(BattleServerController sc) {
        if (player1 == sc) {
            player1 = null;
            if (player2 != null) {
                player2.writeText("-1;Jugador 1 desconectado, se termina la partida.");
            }

            return true;
        } else if (player2 == sc) {
            player2 = null;
            if (player1 != null) {
                player1.writeText("-1;Jugador 1 desconectado, se termina la partida.");
            }
            return true;
        }

        return false;
    }

    public boolean existUsername(String username) {
        return ((player1 != null) && (!player1.getUsername().isEmpty() && player1.getUsername().equals(username)))
                || ((player2 != null) && (!player2.getUsername().isEmpty() && player2.getUsername().equals(username)));
    }

    private boolean sendAttack(BattleServerController source, int row, int column) {

        if (player1 == source) {
            player2.writeText("5000;" + row + ";" + column);
            return true;
        } else if (player2 == source) {
            player1.writeText("5000;" + row + ";" + column);
            return true;
        } else {
            return false;
        }

    }

    private boolean sendAttackResponse(BattleServerController source, int row, int column, int value) {

        if (player1 == source) {
            player2.writeText("6000;" + row + ";" + column + ";" + value);
            return true;
        } else if (player2 == source) {
            player1.writeText("6000;" + row + ";" + column + ";" + value);
            return true;
        } else {
            return false;
        }
    }

    public String responseCommand(String aCmd, BattleServerController source) {
        String response = "";

        if (aCmd.trim().toUpperCase().startsWith("ATTACK;")) {

            String parameters[] = aCmd.substring(7).split(";");
            if (parameters.length >= 2) {

                try {
                    int row = Integer.parseInt(parameters[0]);
                    int column = Integer.parseInt(parameters[1]);

                    if (sendAttack(source, row, column)) {
                        response = "5100;Ataque enviado.";
                    } else {
                        response = "5200;No hay a quien enviarle el ataque";
                    }
                } catch (NumberFormatException e) {
                    response = "0;Parametros invalidos.";
                }
                
                source.writeText(response);

            } else {
                response = "0;Parametros insuficientes.";
            }

        } else if (aCmd.trim().toUpperCase().startsWith("ATTACK_RES;")) {

            System.out.println(aCmd);
            String parameters[] = aCmd.substring(10).split(";");
            
            if (parameters.length >= 4) {

                try {

                    int row = Integer.parseInt(parameters[1]);
                    int column = Integer.parseInt(parameters[2]);
                    int value = Integer.parseInt(parameters[3]);//1 ataco, 0 fail

                    if (sendAttackResponse(source, row, column, value)) {
                        response = "6100;Respuesta del ataque enviada.";
                    } else {
                        response = "6200;No hay a quien enviarle la respuesta del ataque.";
                    }
                } catch (NumberFormatException e) {
                    response = "0;Parametros invalidos.";
                }
            } else {
                response = "0;Parametros insuficientes.";
            }
            
            source.writeText(response);

        } else if (aCmd.trim().toUpperCase().startsWith("REGISTER;")) {

            String username = aCmd.substring(9).trim().toLowerCase();
            if (source.getUsername().isEmpty()) {

                if (!existUsername(username)) {

                    if (registerPlayer1(source)) {
                        response = "1000;Usuario registrado, esperando por el segundo jugador...";
                    } else if (registerPlayer2(source)) {
                        response = "2000;Usuario registrado, listo para iniciar la partida.";
                        player1.writeText("0;Segundo usuario registrado, listo para iniciar la partida");
                    }

                } else {
                    response = "1002;Este nombre de usuario ya existe.";
                }
            } else {
                response = "1003;El usuario ya se ha registrado";
            }
        }else if(aCmd.trim().toUpperCase().startsWith("NOT_READY;")) {
            
            if(player1 == source) {
                player2.writeText("9999;Tu rival aún no está listo");
            } else {
                player1.writeText("9999;Tu rival aún no está listo");
            }
            
            response = "9999;Te estan esperando...";
            
        }else if(aCmd.trim().toUpperCase().startsWith("LOSER;")) {
            if(player1 == source) {
                player2.writeText("9999;Has ganado!");
            } else {
                player1.writeText("9999;Has ganado!");
            }
        } else if(aCmd.trim().toUpperCase().startsWith("EXIT")){
            remove(source);
            System.exit(0);
        }else {
            response = "¿?";
        }
        
        return response;
    }

    public BattleServerController getPlayer1() {
        return player1;
    }

    public void setPlayer1(BattleServerController player1) {
        this.player1 = player1;
    }

    public BattleServerController getPlayer2() {
        return player2;
    }

    public void setPlayer2(BattleServerController player2) {
        this.player2 = player2;
    }
}
